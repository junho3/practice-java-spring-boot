package com.example.demo.config.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class OpenAiConfig {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Bean
    public OpenAiApi openAiApi() {
        log.debug("OpenAI API 클라이언트 초기화");

        return OpenAiApi.builder()
            .apiKey(apiKey)
            .build();
    }
}
