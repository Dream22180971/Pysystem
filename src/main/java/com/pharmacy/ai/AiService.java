package com.pharmacy.ai;

import com.pharmacy.kb.KbService;
import com.pharmacy.kb.KbModels.KbSearchHit;
import com.pharmacy.kb.KbModels.KbSearchResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

import static com.pharmacy.ai.AiModels.Citation;
import static com.pharmacy.ai.AiModels.ChatResponse;

@Service
public class AiService {
    public static final String ASSISTANT_NAME = "药智助手";

    private final KbService kbService;
    private final AliAiClient aliAiClient;

    public AiService(KbService kbService, AliAiClient aliAiClient) {
        this.kbService = kbService;
        this.aliAiClient = aliAiClient;
    }

    public ChatResponse chat(String message, Integer topK) {
        int k = topK == null ? 6 : Math.min(12, Math.max(1, topK));
        KbSearchResult sr = kbService.search(message, k);
        List<KbSearchHit> hits = sr.hits();

        List<Citation> citations = new ArrayList<>();
        StringJoiner ctx = new StringJoiner("\n\n---\n\n");
        for (KbSearchHit h : hits) {
            citations.add(new Citation(h.docPath(), h.docTitle()));
            ctx.add("【" + h.docTitle() + "】(" + h.docPath() + ")\n" + h.snippet());
        }

        List<String> clarifying = buildClarifyingQuestions(message, hits);
        String kbOnly = kbOnlyAnswer(message, hits, citations);

        if (!aliAiClient.isEnabled()) {
            return new ChatResponse(ASSISTANT_NAME, kbOnly, citations, false, clarifying);
        }

        String systemPrompt = """
你是“药智助手”，为药房后台系统员工提供内部流程与系统使用帮助。
要求：
1) 仅基于给定“知识库片段”回答；如果片段不足以回答，就明确说明缺少信息，并给出需要补充的字段/步骤。
2) 输出结构固定为：结论（1句）/ 操作步骤（编号）/ 注意事项/ 引用来源（列出标题与路径）。
3) 不要编造接口、页面或字段名。
""".trim();

        String userPrompt = buildUserPrompt(message, ctx.toString(), citations);

        String reply;
        try {
            reply = aliAiClient.chat(systemPrompt, userPrompt);
        } catch (Exception e) {
            // 调用失败时兜底到 KB-only
            reply = kbOnly + "\n\n（模型调用失败，已降级为知识库检索结果：" + e.getMessage() + "）";
            return new ChatResponse(ASSISTANT_NAME, reply, citations, false, clarifying);
        }

        return new ChatResponse(ASSISTANT_NAME, reply, citations, true, clarifying);
    }

    private static String buildUserPrompt(String message, String context, List<Citation> citations) {
        StringBuilder sb = new StringBuilder();
        sb.append("用户问题：").append(message).append("\n\n");
        sb.append("知识库片段：\n").append(context.isBlank() ? "（无命中）" : context).append("\n\n");
        sb.append("引用来源（用于你在回答末尾列出）：\n");
        if (citations.isEmpty()) {
            sb.append("（无）\n");
        } else {
            for (Citation c : citations) {
                sb.append("- ").append(c.title()).append(" (").append(c.path()).append(")\n");
            }
        }
        return sb.toString();
    }

    private static String kbOnlyAnswer(String message, List<KbSearchHit> hits, List<Citation> citations) {
        if (hits.isEmpty()) {
            return """
结论：当前知识库未检索到相关内容。

操作步骤：
1) 请换一种问法（包含页面名称/报错码/功能点）。
2) 或把对应SOP/系统说明补充到本地 kb/ 目录后，再重试。

注意事项：
- 我目前仅基于本地 Markdown 知识库回答。

引用来源：
（无）
""".trim();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("结论：我从知识库中找到了与“").append(message).append("”最相关的操作说明。\n\n");
        sb.append("操作步骤：\n");
        sb.append("1) 参考下方命中片段中的步骤执行。\n");
        sb.append("2) 若仍失败，请把具体页面/报错信息补充给我，我再继续定位。\n\n");
        sb.append("注意事项：\n");
        sb.append("- 以下为知识库命中摘要，必要时请打开原文查看完整上下文。\n\n");
        sb.append("引用来源：\n");
        for (Citation c : citations) {
            sb.append("- ").append(c.title()).append(" (").append(c.path()).append(")\n");
        }
        sb.append("\n命中片段：\n");
        int i = 1;
        for (KbSearchHit h : hits) {
            sb.append("\n[").append(i++).append("] ").append(h.docTitle()).append(" (").append(h.docPath()).append(")\n");
            sb.append(h.snippet()).append("\n");
        }
        return sb.toString().trim();
    }

    private static List<String> buildClarifyingQuestions(String message, List<KbSearchHit> hits) {
        // 目标：流程问答更强。信息不足时，引导用户补齐最关键的分支条件。
        // 规则：无命中或命中很少时给更多追问；命中充分时只给少量可选追问。
        int base = hits.isEmpty() ? 4 : (hits.size() <= 1 ? 3 : 2);
        List<String> qs = new ArrayList<>();

        String m = message == null ? "" : message.trim();
        String ml = m.toLowerCase(Locale.ROOT);

        // 角色/权限几乎总是关键
        qs.add("你当前使用的是管理员还是员工账号？（ROLE_ADMIN / ROLE_EMP）");

        // 模块/页面入口
        if (!containsAny(m, "采购", "入库", "库存", "盘点", "销售", "药品", "分类", "登录", "权限", "审计", "报表")) {
            qs.add("这个问题发生在哪个模块页面？（如：采购管理 / 库存管理 / 登录页 / 日志审计）");
        }

        // 操作类型
        if (!containsAny(m, "新增", "添加", "修改", "更新", "删除", "查询", "搜索", "导出", "保存", "提交")) {
            qs.add("你要完成的具体操作是什么？（新增/修改/删除/查询/导出）");
        }

        // 报错信息
        if (!ml.contains("401") && !ml.contains("403") && !containsAny(m, "报错", "错误", "失败", "无权限", "看不到", "无法")) {
            qs.add("是否有报错提示或状态码？（例如 401/403/字段校验失败）");
        } else if (!containsAny(m, "401", "403") && containsAny(m, "无权限", "看不到", "权限")) {
            qs.add("页面提示的具体文案是什么？是否是 403 权限不足？");
        }

        // 如果已有命中，则追问更聚焦
        if (!hits.isEmpty() && containsAny(m, "怎么", "如何", "步骤", "流程")) {
            qs.add("你现在卡在第几步？（请描述你点了什么按钮、期望发生什么）");
        }

        // 截断到 base~4
        if (qs.size() > Math.max(base, 2)) {
            return qs.subList(0, Math.max(base, 2));
        }
        return qs;
    }

    private static boolean containsAny(String s, String... needles) {
        if (s == null) return false;
        for (String n : needles) {
            if (n != null && !n.isBlank() && s.contains(n)) return true;
        }
        return false;
    }
}

