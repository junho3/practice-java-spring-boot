package com.example.demo.config.async;

import org.springframework.boot.task.SimpleAsyncTaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class DomainEventAsyncConfig {

    // application.yml에 spring.threads.virtual.enabled: true 버츄얼 스레드가 활성화 되있다면, @Async 선언 시 기본적으로 버츄얼 스레드를 사용함
    // @Primary를 선언하지 않아도 메소드명이 taskExecutor라면 기본 빈으로 등록 됨
    @Bean
    public SimpleAsyncTaskExecutor taskExecutor(SimpleAsyncTaskExecutorBuilder builder) {
        return builder.build();
    }

    // 플랫폼 스레드용 Bean 정의, @Async("domainEventAsyncExecutor")로 선언하면 플랫폼 스레드를 사용함
    @Bean("domainEventAsyncExecutor")
    public Executor domainEventAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(30);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("domainEventAsyncExecutor-");
        return executor;
    }
}
