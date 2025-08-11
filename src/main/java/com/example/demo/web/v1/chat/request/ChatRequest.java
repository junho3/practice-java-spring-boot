package com.example.demo.web.v1.chat.request;

import jakarta.validation.constraints.NotEmpty;

public record ChatRequest(
    @NotEmpty String query,
    String model
) {
    public ChatRequest {
        model = model == null ? "gpt-3.5-turbo" : model;
    }
}
