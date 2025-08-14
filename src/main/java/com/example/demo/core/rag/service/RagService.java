package com.example.demo.core.rag.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RagService {

    private final ExtractPdfFileService extractPdfFileService;

    public String uploadPdfFile(final File file, final String originalFilename) {
        // documentId 채번
        final String documentId = UUID.randomUUID().toString();

        log.info("PDF 문서 업로드 시작. 파일: {}, ID: {}", originalFilename, documentId);

        // 메타데이터 준비
        final Map<String, Object> documentMetaData = Map.of(
            "id", documentId,
            "originalFilename", originalFilename != null ? originalFilename : "",
            "uploadTime", System.currentTimeMillis()
        );

        // 텍스트 추출
        final String text = extractPdfFileService.extract(file);

        // Spring AI Document 객체 생성
        log.info("문서 추가 시작 - ID {}, 내용 길이: {}", documentId, text.length());

        final Document document = new Document(text, documentMetaData);
        final TokenTextSplitter textSplitter = TokenTextSplitter.builder()
            .withChunkSize(512)           // 원하는 청크 크기
            .withMinChunkSizeChars(350)   // 최소 청크 크기
            .withMinChunkLengthToEmbed(5) // 임베딩할 최소 청크 길이
            .withMaxNumChunks(10000)      // 최대 청크 수
            .withKeepSeparator(true)      // 구분자 유지 여부
            .build();
        final List<Document> chunks = textSplitter.split(document);

        // 벡터 스토어에 문서 청크 추가 (내부적으로 임베딩 변환 수행)

        return documentId;
    }
}
