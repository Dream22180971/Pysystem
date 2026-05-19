package com.pharmacy.kb;

import java.time.Instant;
import java.util.List;

/**
 * 本地 Markdown 知识库的简单模型。
 * 说明：MVP 先采用文件扫描 + 内存索引 + 关键词检索，避免引入额外依赖与数据库改造。
 */
public final class KbModels {
    private KbModels() {}

    public record KbDoc(
            String path,
            String title,
            Instant updatedAt
    ) {}

    public record KbSearchHit(
            String docPath,
            String docTitle,
            double score,
            String snippet
    ) {}

    public record KbSearchResult(
            List<KbSearchHit> hits,
            int total
    ) {}
}

