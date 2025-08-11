package com.example.demo.core.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final OpenAiApi openAiApi;

    public ChatResponse openAiChat(
        final String userInput,
        final String systemMessage,
        final String model
    ) {
        log.debug("OpenAI 챗 호출 시작 - 모델: {}", model);

        try {
            // 메시지 구성
            final List<Message> messages = List.of(
                new SystemMessage(systemMessage),
                new UserMessage(userInput)
            );

            // 챗 옵션 설정
            final ChatOptions chatOptions = ChatOptions.builder()
                .model(model)
                .build();

            // 프롬프트 생성
            final Prompt prompt = new Prompt(messages, chatOptions);

            // 챗 모델 생성 및 호출
            final OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .build();

            return chatModel.call(prompt);
        } catch (final Exception e) {
            log.error("OpenAI 챗 호출 중 오류 발생: {}", e.getMessage());
            return null;
        }
    }
}
