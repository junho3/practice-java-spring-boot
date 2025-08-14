package com.example.demo.core.rag.service;

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
        log.debug("PDF 텍스트 추출 시작: {}", file.getName());

        try {
            final PDDocument document = PDDocument.load(file);
            log.debug("PDF 문서 로드 성공: {} 페이지", document.getNumberOfPages());

            return new PDFTextStripper().getText(document);
        } catch (final IOException e) {
            log.error("PDF 텍스트 추출 실패");
            return "";
        }
//        return try {
//            // Apache PDFBox를 사용하여 PDF에서 텍스트 추출
//            PDDocument.load(pdfFile).use { document ->
//                logger.debug { "PDF 문서 로드 성공: ${document.numberOfPages}페이지" }
//                PDFTextStripper().getText(document)
//            }.also {
//                logger.debug { "PDF 텍스트 추출 완료: ${it.length} 문자" }
//            }
//        } catch (e: IOException) {
//            logger.error(e) { "PDF 텍스트 추출 실패" }
//            throw DocumentProcessingException("PDF에서 텍스트 추출 실패: ${e.message}", e)
//        }
    }
}
