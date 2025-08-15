package com.example.demo.core.ai.service;

import com.example.demo.core.ai.domain.SimilarityDocument;
import com.example.demo.infrastructure.persistence.ai.VectorStoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RagService {

    private final ExtractPdfFileService extractPdfFileService;
    private final VectorStoreRepository vectorStoreRepository;

    public String uploadPdfFile(final File file, final String originalFilename) {
        // documentId 채번
        final String documentId = UUID.randomUUID().toString();

        log.info("PDF 문서 업로드 시작. 파일: {}, ID: {}", originalFilename, documentId);

        // 텍스트 추출
        final String text = extractPdfFileService.extract(file);

        // Spring AI Document 객체 생성
        log.info("Document 객체 생성 시작 - ID {}, 내용 길이: {}", documentId, text.length());

        // 메타데이터 준비
        final Map<String, Object> documentMetaData = Map.of(
            "id", documentId,
            "originalFilename", originalFilename != null ? originalFilename : "",
            "uploadTime", System.currentTimeMillis()
        );
        final Document document = new Document(text, documentMetaData);

        log.info("Document 객체 생성 완료 - ID: {}", documentId);

        // 벡터 스토어에 문서 청크 추가 (내부적으로 임베딩 변환 수행)
        vectorStoreRepository.add(document);

        return documentId;
    }

    public List<SimilarityDocument> retrieve(final String question, final int maxResults) {
        log.info("검색 시작: {}, 최대 결과 수: {}", question, maxResults);
        final List<Document> documents = vectorStoreRepository.similaritySearch(question, maxResults);

        // 결과 매핑
        return documents.stream()
            .map(it -> new SimilarityDocument(
                it.getMetadata().getOrDefault("id", "unknown").toString(),
                it.getText() == null ? "" : it.getText(),
                it.getMetadata().entrySet().stream()
                    .filter(entry -> !"id".equals(entry.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),
                it.getScore() == null ? 0.0 : it.getScore()
            ))
            .toList();
    }
}
