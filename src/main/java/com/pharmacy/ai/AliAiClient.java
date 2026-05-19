package com.pharmacy.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 阿里 DashScope/百炼 OpenAI 兼容 Chat Completions 客户端。
 *
 * 默认 endpoint（中国区）：https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions
 * Header：Authorization: Bearer {apiKey}
 */
@Component
public class AliAiClient {
    private final RestTemplate restTemplate;
    private final String endpoint;
    private final String apiKey;
    private final String model;

    public AliAiClient(
            @Value("${app.ai.ali.endpoint:https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions}") String endpoint,
            @Value("${app.ai.ali.api-key:}") String apiKey,
            @Value("${app.ai.ali.model:qwen-max}") String model
    ) {
        this.restTemplate = new RestTemplate();
        this.endpoint = endpoint;
        this.apiKey = apiKey;
        this.model = model;
    }

    public boolean isEnabled() {
        return apiKey != null && !apiKey.isBlank();
    }

    public String chat(String systemPrompt, String userPrompt) {
        if (!isEnabled()) {
            throw new IllegalStateException("Ali AI api-key not configured");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(apiKey.trim());

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userPrompt)
        ));
        body.put("temperature", 0.2);

        try {
            ResponseEntity<Map> resp = restTemplate.postForEntity(endpoint, new HttpEntity<>(body, headers), Map.class);
            Map<?, ?> m = resp.getBody();
            if (m == null) throw new IllegalStateException("empty response");
            // OpenAI compatible: choices[0].message.content
            Object choices = m.get("choices");
            if (!(choices instanceof List<?> cl) || cl.isEmpty()) throw new IllegalStateException("missing choices");
            Object c0 = cl.get(0);
            if (!(c0 instanceof Map<?, ?> c0m)) throw new IllegalStateException("invalid choices[0]");
            Object msg = c0m.get("message");
            if (!(msg instanceof Map<?, ?> msgm)) throw new IllegalStateException("invalid message");
            Object content = msgm.get("content");
            if (!(content instanceof String s)) throw new IllegalStateException("invalid content");
            return s.trim();
        } catch (RestClientException e) {
            throw new IllegalStateException("Ali AI call failed: " + e.getMessage(), e);
        }
    }
}

