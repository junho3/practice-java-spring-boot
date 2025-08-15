package com.example.demo.infrastructure.persistence.ai;

import org.springframework.ai.document.Document;

import java.util.List;

public interface VectorStoreRepository {

    void add(Document document);

    List<Document> similaritySearch(String query, int topK);
}
