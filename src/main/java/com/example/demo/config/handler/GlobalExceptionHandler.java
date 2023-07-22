package com.example.demo.config.handler;

import com.example.demo.web.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String DEFAULT_INTERNAL_SERVER_ERR_MSG = "알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.";
    private static final String DEFAULT_BAD_REQUEST_ERR_MSG = "잘못된 요청입니다. 요청내용을 확인하세요.";

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    protected ApiResponse<Void> handle(Exception exception) {
        log.error(exception.getMessage(), exception);

        return ApiResponse.fail("500", DEFAULT_INTERNAL_SERVER_ERR_MSG);
    }
}
