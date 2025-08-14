package com.example.demo.infrastructure.persistence.rag;

import org.springframework.ai.document.Document;

public interface VectorStoreRepository {

    void add(Document document);
}
