package com.example.demo.web.v1.ai.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public record QueryRequest(
    @Schema(description = "사용자 질문", example = "인공지능이란 무엇인가요?")
    @NotEmpty
    String query,

    @Schema(description = "최대 검색 결과 수", example = "3", defaultValue = "3")
    @Positive
    int maxResults,

    @Schema(description = "사용할 LLM 모델", example = "gpt-3.5-turbo", defaultValue = "gpt-3.5-turbo")
    String model
) {
}
