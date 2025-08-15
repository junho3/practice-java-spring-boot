package com.example.demo.infrastructure.persistence.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class EmbeddingVectorStoreRepositoryImpl implements VectorStoreRepository {

    private final VectorStore vectorStore;

    public EmbeddingVectorStoreRepositoryImpl(final OpenAiEmbeddingModel openAiEmbeddingModel) {
        this.vectorStore = SimpleVectorStore.builder(openAiEmbeddingModel).build();
    }

    @Override
    public void add(final Document document) {
        log.info("Vector 저장 시작 - ID {}", document.getMetadata().get("id"));

        final TokenTextSplitter textSplitter = TokenTextSplitter.builder()
            .withChunkSize(512) // 원하는 청크 크기
            .withMinChunkSizeChars(350) // 최소 청크 크기
            .withMinChunkLengthToEmbed(5) // 임베딩할 최소 청크 길이
            .withMaxNumChunks(10000) // 최대 청크 수
            .withKeepSeparator(true) // 구분자 유지 여부
            .build();
        final List<Document> chunks = textSplitter.split(document);

        vectorStore.add(chunks);

        log.info("Vector 저장 완료 - ID {}", document.getMetadata().get("id"));
    }

    @Override
    public List<Document> similaritySearch(final String query, final int topK) {
        log.info("유사도 검색 시작 - 질의: {}, 최대 결과: {}", query, topK);

        // 유사성 검색 실행
        final List<Document> documents = vectorStore.similaritySearch(
            SearchRequest.builder()
                .query(query)
                .topK(topK)
                .build()
        );

        if (documents == null) {
            log.info("유사도 검색 완료 - 결과 없음");
            return List.of();
        }

        log.info("유사도 검색 완료 - 결과 수: {}", documents.size());

        return documents;
    }
}
