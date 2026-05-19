package com.pharmacy.controller;

import com.pharmacy.kb.KbService;
import com.pharmacy.util.ResultJson;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 本地 Markdown 知识库接口：
 * - 列表：GET /api/kb/docs
 * - 检索：GET /api/kb/search?q=...
 * - 读取：GET /api/kb/doc?path=...
 * - 重建索引：POST /api/kb/resync（管理员）
 */
@RestController
@RequestMapping("/api/kb")
public class KbController {

    private final KbService kbService;

    public KbController(KbService kbService) {
        this.kbService = kbService;
    }

    @GetMapping("/docs")
    public ResultJson docs() {
        Map<String, Object> data = new HashMap<>();
        data.put("root", kbService.getKbRoot().toString());
        data.put("docs", kbService.listDocs());
        return ResultJson.success(data);
    }

    @GetMapping("/doc")
    public ResultJson doc(@RequestParam("path") String path) {
        var meta = kbService.getDocMeta(path);
        if (meta == null) {
            return ResultJson.error(404, "文档不存在：" + path);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("meta", meta);
        data.put("content", kbService.getDocContent(path));
        return ResultJson.success(data);
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultJson search(
            @RequestParam("q") String q,
            @RequestParam(value = "limit", required = false, defaultValue = "8") Integer limit
    ) {
        int lim = limit == null ? 8 : limit;
        return ResultJson.success(kbService.search(q, lim));
    }

    @PostMapping("/resync")
    public ResultJson resync() {
        try {
            int count = kbService.resync();
            Map<String, Object> data = new HashMap<>();
            data.put("count", count);
            data.put("root", kbService.getKbRoot().toString());
            return ResultJson.success(data);
        } catch (IOException e) {
            return ResultJson.error(500, "重建索引失败：" + e.getMessage());
        }
    }
}

