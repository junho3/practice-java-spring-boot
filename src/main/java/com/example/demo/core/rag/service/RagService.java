package com.example.demo.core.rag.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class RagService {

    public String uploadPdfFile(final File file, final String originalFilename) {
        // documentId 채번
        final String documentId = UUID.randomUUID().toString();

        log.info("PDF 문서 업로드 시작. 파일: {}, ID: {}", originalFilename, documentId);

        // 메타데이터 준비
        final Map<String, Object> docMetaData = Map.of(
            "originalFilename", originalFilename != null ? originalFilename : "",
            "uploadTime", System.currentTimeMillis()
        );

        // 텍스트 추출

        // Spring AI Document 객체 생성

        // 벡터 스토어에 문서 청크 추가 (내부적으로 임베딩 변환 수행)

        return documentId;
    }
}
