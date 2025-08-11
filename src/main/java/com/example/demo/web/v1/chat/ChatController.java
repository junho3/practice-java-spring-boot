package com.example.demo.web.v1.chat;

import com.example.demo.core.chat.service.ChatService;
import com.example.demo.web.ApiResponse;
import com.example.demo.web.v1.chat.request.ChatRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Chat API", description = "OpenAI API를 통한 채팅 기능")
@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/query")
    public ApiResponse<String> sendMessage(@Valid @RequestBody ChatRequest request) {
        log.info("Chat API 요청 받음: model= {}", request.model());

        final ChatResponse chatResponse = chatService.openAiChat(
            request.query(),
            "You are a helpful AI assistant.",
            request.model()
        );

        return ApiResponse.success(chatResponse.getResult().getOutput().getText());
    }
}
