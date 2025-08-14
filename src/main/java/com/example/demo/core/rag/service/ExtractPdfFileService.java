package com.example.demo.core.rag.service;

import com.example.demo.core.rag.ExtractFileException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class ExtractPdfFileService {

    public String extract(final File file) {
        log.info("PDF 텍스트 추출 시작: {}", file.getName());

        try {
            final PDDocument document = PDDocument.load(file);
            log.info("PDF 문서 로드 성공: {} 페이지", document.getNumberOfPages());

            return new PDFTextStripper().getText(document);
        } catch (final IOException e) {
            log.error("PDF 텍스트 추출 실패");
            throw new ExtractFileException("PDF에서 텍스트 추출 실패: " + e.getMessage(), e);
        }
    }
}
