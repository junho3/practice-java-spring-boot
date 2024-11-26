package com.example.demo.config.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@Slf4j
public class DomainEventAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.error("도메인 이벤트 비동기 처리 중 에러 발생 : {}", method.getName(), ex);
        for (Object param : params) {
            log.error("Parameter value: {}", param);
        }
    }
}
