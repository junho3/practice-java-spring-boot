package com.example.demo.web.v1.ai;

import com.example.demo.core.ai.service.RagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Tag(name = "RAG API", description = "Retrieval-Augmented Generation 기능을 위한 API")
@Slf4j
@RestController
@RequestMapping("/api/v1/rag")
@RequiredArgsConstructor
public class RagController {

    private final RagService ragService;

    @Operation(
        summary = "PDF 문서 업로드",
        description = "PDF 파일을 업로드하여 벡터 스토어에 저장합니다. 추후 질의에 활용됩니다."
    )
    @PostMapping(path = "/documents", consumes = MULTIPART_FORM_DATA_VALUE)
    public void uploadDocument(
        @Parameter(description = "업로드할 PDF 파일", required = true)
        @RequestParam("file") MultipartFile file
    ) throws IOException {
        final File tempFile = File.createTempFile("upload_", ".pdf");
        log.info("임시 파일 경로 {}", tempFile.getAbsolutePath());
        file.transferTo(tempFile);

        final String text = ragService.uploadPdfFile(tempFile, file.getOriginalFilename());

        // TODO 컨트롤러에서 수행하는 임시 파일 생성 및 삭제를 서비스 레이어로 내리거나 다른 방법을 찾아야 함
        if (tempFile.exists()) {
            tempFile.delete();
            log.info("임시 파일 삭제됨: {}", tempFile.getAbsolutePath());
        }
    }
}
