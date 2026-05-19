package com.pharmacy.ai;

import java.util.List;

public final class AiModels {
    private AiModels() {}

    public record ChatRequest(
            String message,
            Integer topK
    ) {}

    public record Citation(
            String path,
            String title
    ) {}

    public record ChatResponse(
            String assistantName,
            String reply,
            List<Citation> citations,
            boolean usedModel,
            List<String> clarifyingQuestions
    ) {}
}

