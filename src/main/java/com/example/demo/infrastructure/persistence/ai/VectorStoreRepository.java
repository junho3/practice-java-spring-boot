package com.example.demo.infrastructure.persistence.ai;

import org.springframework.ai.document.Document;

public interface VectorStoreRepository {

    void add(Document document);
}
