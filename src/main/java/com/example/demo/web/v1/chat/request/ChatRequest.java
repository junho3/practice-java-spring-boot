package com.example.demo.web.v1.chat.request;

public record ChatRequest(
    String query,
    String model
) {
    public ChatRequest {
        model = model == null ? "gpt-3.5-turbo" : model;
    }
}
