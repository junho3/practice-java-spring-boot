package com.example.demo.web.v1.rag;

import com.example.demo.core.rag.service.RagService;
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
        file.transferTo(tempFile);

        final String text = ragService.uploadPdfFile(tempFile, file.getOriginalFilename());

        System.out.println(text);
    }
}
