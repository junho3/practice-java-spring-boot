package com.example.demo.config.web.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
            .info(
                new Info()
                    .title("Java Spring Boot Tutorial Application")
                    .version("1.0")
                    .description("Java + Spring Boot 학습을 위한 애플리케이션")
            );
    }
}
