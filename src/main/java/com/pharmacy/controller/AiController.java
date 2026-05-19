package com.pharmacy.controller;

import com.pharmacy.ai.AiModels.ChatRequest;
import com.pharmacy.ai.AiService;
import com.pharmacy.util.ResultJson;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 药智助手：内部流程/系统使用问答（基于本地 kb/ 目录检索；可选调用阿里模型）。
 */
@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    public record ChatBody(
            @NotBlank String message,
            Integer topK
    ) {}

    @PostMapping(value = "/chat", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResultJson chat(@Valid @RequestBody ChatBody body) {
        return ResultJson.success(aiService.chat(body.message(), body.topK()));
    }
}

