package com.example.demo.core.ai.domain;

import java.util.Map;

public record SimilarityDocument(
    String id,
    String content,
    Map<String, Object> metadata,
    double score
) {
}
