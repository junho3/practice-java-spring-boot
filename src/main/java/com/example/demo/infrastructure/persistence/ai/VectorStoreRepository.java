package com.example.demo.infrastructure.persistence.ai;

import com.example.demo.core.ai.result.DocumentSearchResult;
import org.springframework.ai.document.Document;

import java.util.List;

public interface VectorStoreRepository {

    void add(Document document);

    List<DocumentSearchResult> similaritySearch(String query, int topK);
}
