package com.example.demo.core.ai.result;

import java.util.Map;

public record DocumentSearchResult(
    String id,
    String content,
    Map<String, Object> metadata,
    double score
) {
}
