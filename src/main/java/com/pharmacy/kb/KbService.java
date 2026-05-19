package com.pharmacy.kb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.pharmacy.kb.KbModels.KbDoc;
import static com.pharmacy.kb.KbModels.KbSearchHit;
import static com.pharmacy.kb.KbModels.KbSearchResult;

/**
 * 本地 Markdown 知识库：扫描目录下的 .md 文件，建立内存索引并提供关键词检索/读取。
 *
 * 约束（MVP）：
 * - 不做向量化；仅做简单词匹配与片段提取
 * - 只索引 .md 文本内容；图片/附件不参与检索
 */
@Service
public class KbService {

    private final Path kbRoot;

    /** docPath -> doc */
    private volatile Map<String, DocIndex> index = Map.of();

    public KbService(@Value("${app.kb.path:kb}") String kbPath) {
        this.kbRoot = normalizeRoot(kbPath);
    }

    @PostConstruct
    public void init() {
        // 启动时构建一次索引；失败不阻塞启动（KB 可为空）
        try {
            resync();
        } catch (Exception ignored) {
            // ignore
        }
    }

    public Path getKbRoot() {
        return kbRoot;
    }

    public synchronized int resync() throws IOException {
        Map<String, DocIndex> next = new HashMap<>();
        if (!Files.exists(kbRoot) || !Files.isDirectory(kbRoot)) {
            index = Map.of();
            return 0;
        }

        try (Stream<Path> s = Files.walk(kbRoot)) {
            List<Path> files = s
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".md"))
                    .sorted(Comparator.comparing(Path::toString))
                    .toList();

            for (Path p : files) {
                String rel = toRelPath(p);
                String content = Files.readString(p, StandardCharsets.UTF_8);
                Instant updatedAt = Instant.ofEpochMilli(Files.getLastModifiedTime(p).toMillis());
                String title = extractTitle(rel, content);
                List<String> chunks = chunkMarkdown(content, 800);
                next.put(rel, new DocIndex(new KbDoc(rel, title, updatedAt), content, chunks));
            }
        }

        index = Map.copyOf(next);
        return index.size();
    }

    public List<KbDoc> listDocs() {
        return index.values().stream()
                .map(d -> d.doc)
                .sorted(Comparator.comparing(KbDoc::path))
                .toList();
    }

    public KbDoc getDocMeta(String path) {
        DocIndex d = index.get(path);
        return d == null ? null : d.doc;
    }

    public String getDocContent(String path) {
        DocIndex d = index.get(path);
        return d == null ? null : d.raw;
    }

    public KbSearchResult search(String q, int limit) {
        String query = normalizeQuery(q);
        if (query == null) return new KbSearchResult(List.of(), 0);
        List<String> terms = extractTerms(query);
        int lim = Math.min(50, Math.max(1, limit));

        List<KbSearchHit> hits = new ArrayList<>();
        for (DocIndex d : index.values()) {
            Hit best = bestHit(d, terms);
            if (best != null) {
                hits.add(new KbSearchHit(
                        d.doc.path(),
                        d.doc.title(),
                        best.score,
                        best.snippet
                ));
            }
        }

        List<KbSearchHit> top = hits.stream()
                .sorted(Comparator.comparingDouble(KbSearchHit::score).reversed())
                .limit(lim)
                .toList();

        return new KbSearchResult(top, hits.size());
    }

    private Hit bestHit(DocIndex d, List<String> terms) {
        // 在 chunks 中找最优片段（命中次数*权重）
        double bestScore = 0;
        String bestSnippet = null;

        for (String c : d.chunks) {
            ScoreAndTerm st = scoreChunk(c, terms);
            double score = st.score;
            if (score > bestScore) {
                bestScore = score;
                bestSnippet = makeSnippet(c, st.bestTerm, 220);
            }
        }
        if (bestScore <= 0 || bestSnippet == null) return null;

        // 流程类文档：标题/路径命中通常更强信号（如“采购入库 SOP”）
        double boost = scoreTitleAndPath(d.doc.title(), d.doc.path(), terms);
        return new Hit(bestScore + boost, bestSnippet);
    }

    private static ScoreAndTerm scoreChunk(String chunk, List<String> terms) {
        String hay = chunk.toLowerCase(Locale.ROOT);
        double score = 0;
        String bestTerm = terms.isEmpty() ? "" : terms.get(0);
        int bestCount = 0;
        for (String t : terms) {
            String needle = t.toLowerCase(Locale.ROOT);
            if (needle.isBlank()) continue;
            int count = countOccurrences(hay, needle);
            if (count > 0) {
                // 简单权重：更长的 term 更有信息量
                double w = Math.min(2.5, Math.max(1.0, needle.length() / 2.0));
                score += count * 10.0 * w;
                if (count > bestCount) {
                    bestCount = count;
                    bestTerm = t;
                }
            }
        }
        if (score <= 0) return new ScoreAndTerm(0, bestTerm);
        // 稍微偏好更短的片段（流程类文档可减少噪声）
        double lenPenalty = Math.max(0.6, 1.0 - (chunk.length() / 2000.0));
        return new ScoreAndTerm(score * lenPenalty, bestTerm);
    }

    private static double scoreTitleAndPath(String title, String path, List<String> terms) {
        String t = (title == null ? "" : title).toLowerCase(Locale.ROOT);
        String p = (path == null ? "" : path).toLowerCase(Locale.ROOT);
        double s = 0;
        for (String term : terms) {
            if (term == null || term.isBlank()) continue;
            String needle = term.toLowerCase(Locale.ROOT);
            if (needle.length() < 2) continue;
            int c1 = countOccurrences(t, needle);
            int c2 = countOccurrences(p, needle);
            if (c1 > 0) s += c1 * 18.0; // 标题命中更强
            if (c2 > 0) s += c2 * 10.0; // 路径命中次之
        }
        return Math.min(120.0, s);
    }

    private static int countOccurrences(String haystack, String needle) {
        int c = 0;
        int i = 0;
        while (true) {
            int idx = haystack.indexOf(needle, i);
            if (idx < 0) return c;
            c++;
            i = idx + Math.max(1, needle.length());
        }
    }

    private static String makeSnippet(String chunk, String query, int maxLen) {
        String hay = chunk;
        String q = query;
        int idx = chunk.toLowerCase(Locale.ROOT).indexOf(q.toLowerCase(Locale.ROOT));
        if (idx < 0) {
            return trim(chunk, maxLen);
        }
        int start = Math.max(0, idx - 80);
        int end = Math.min(chunk.length(), idx + q.length() + 120);
        String sub = chunk.substring(start, end).replaceAll("\\s+", " ").trim();
        if (start > 0) sub = "…" + sub;
        if (end < chunk.length()) sub = sub + "…";
        return trim(sub, maxLen);
    }

    private static List<String> extractTerms(String q) {
        // 先按常见中英文标点/空白粗分词；不足时保留原句
        String normalized = q
                .replaceAll("[\\p{Punct}，。！？；：、“”‘’（）【】《》]+", " ")
                .replaceAll("\\s+", " ")
                .trim();
        if (normalized.isBlank()) return List.of(q);
        String[] parts = normalized.split(" ");
        List<String> terms = new ArrayList<>();
        for (String p : parts) {
            String t = p.trim();
            if (t.isBlank()) continue;
            // 过滤过短 token（中文 1 字意义弱）
            if (t.length() < 2) continue;
            if (!terms.contains(t)) terms.add(t);
        }
        // 中文场景：额外抽取 2~4 字片段，提升无空格句子的命中率（避免引入分词依赖）
        String compact = normalized.replace(" ", "");
        if (containsCjk(compact)) {
            int maxNgrams = 60;
            for (int n = 4; n >= 2; n--) {
                for (int i = 0; i + n <= compact.length(); i++) {
                    String ng = compact.substring(i, i + n);
                    if (ng.isBlank()) continue;
                    if (!terms.contains(ng)) terms.add(ng);
                    if (terms.size() >= maxNgrams) break;
                }
                if (terms.size() >= maxNgrams) break;
            }
        }
        if (terms.isEmpty()) return List.of(q);
        // 同时加入原句（用于精确匹配时加分）
        if (!terms.contains(q)) terms.add(q);
        return terms;
    }

    private static boolean containsCjk(String s) {
        for (int i = 0; i < s.length(); i++) {
            Character.UnicodeScript sc = Character.UnicodeScript.of(s.charAt(i));
            if (sc == Character.UnicodeScript.HAN) return true;
        }
        return false;
    }

    private static String trim(String s, int maxLen) {
        String t = s.trim();
        if (t.length() <= maxLen) return t;
        return t.substring(0, Math.max(0, maxLen - 1)).trim() + "…";
    }

    private static List<String> chunkMarkdown(String md, int maxChars) {
        // 基于空行做粗切分，再合并到 maxChars 左右
        List<String> parts = Stream.of(md.split("\\R\\s*\\R+"))
                .map(String::trim)
                .filter(p -> !p.isBlank())
                .toList();

        List<String> out = new ArrayList<>();
        StringBuilder buf = new StringBuilder();
        for (String p : parts) {
            if (buf.length() == 0) {
                buf.append(p);
                continue;
            }
            if (buf.length() + 2 + p.length() <= maxChars) {
                buf.append("\n\n").append(p);
            } else {
                out.add(buf.toString());
                buf.setLength(0);
                buf.append(p);
            }
        }
        if (buf.length() > 0) out.add(buf.toString());

        // 兜底：防止单段过长
        return out.stream()
                .flatMap(s -> {
                    if (s.length() <= maxChars) return Stream.of(s);
                    List<String> chunks = new ArrayList<>();
                    for (int i = 0; i < s.length(); i += maxChars) {
                        chunks.add(s.substring(i, Math.min(s.length(), i + maxChars)));
                    }
                    return chunks.stream();
                })
                .collect(Collectors.toList());
    }

    private static String extractTitle(String relPath, String content) {
        try (Stream<String> lines = content.lines()) {
            String h1 = lines
                    .map(String::trim)
                    .filter(l -> l.startsWith("# "))
                    .map(l -> l.substring(2).trim())
                    .filter(l -> !l.isBlank())
                    .findFirst()
                    .orElse(null);
            if (h1 != null) return h1;
        }
        String name = Paths.get(relPath).getFileName().toString();
        if (name.toLowerCase(Locale.ROOT).endsWith(".md")) {
            name = name.substring(0, name.length() - 3);
        }
        return name;
    }

    private String toRelPath(Path p) {
        Path rel = kbRoot.relativize(p);
        // API 统一用 / 分隔，避免 Windows 反斜杠
        return rel.toString().replace('\\', '/');
    }

    private static Path normalizeRoot(String kbPath) {
        Objects.requireNonNull(kbPath, "kbPath");
        Path p = Paths.get(kbPath);
        if (!p.isAbsolute()) {
            p = Paths.get(System.getProperty("user.dir")).resolve(p);
        }
        return p.normalize();
    }

    private static String normalizeQuery(String q) {
        if (q == null) return null;
        String t = q.trim();
        if (t.isBlank()) return null;
        // 避免超长输入导致 CPU 扫描开销过大
        if (t.length() > 200) t = t.substring(0, 200);
        return t;
    }

    private record DocIndex(KbDoc doc, String raw, List<String> chunks) {}

    private record Hit(double score, String snippet) {}

    private record ScoreAndTerm(double score, String bestTerm) {}
}

