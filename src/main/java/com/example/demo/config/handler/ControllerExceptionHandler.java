package com.example.demo.config.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.Optional;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String DEFAULT_INTERNAL_SERVER_ERR_MESSAGE = "알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.";
    private static final String DEFAULT_BAD_REQUEST_ERR_MESSAGE = "잘못된 요청입니다. 요청내용을 확인하세요.";

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ProblemDetail> handle(Exception e) {
        log.error(e.getMessage(), e);

        return ResponseEntity
            .status(INTERNAL_SERVER_ERROR)
            .body(getProblemDetail(INTERNAL_SERVER_ERROR, e, getErrorMessage(e)));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        return ResponseEntity
            .status(status.value())
            .body(getProblemDetail(HttpStatus.valueOf(status.value()), ex, getErrorMessage(ex)));
    }

    // 에러 메시지 생성
    private String getErrorMessage(final Exception exception) {
        return switch (exception) {
            case MethodArgumentNotValidException e -> Optional.of(e.getDetailMessageArguments())
                .map(args -> Arrays.stream(args)
                    .filter(msg -> !ObjectUtils.isEmpty(msg))
                    .reduce("Please make sure to provide a valid request, ", (a, b) -> a + " " + b)
                )
                .orElse("").toString();
            default -> exception.getMessage();
        };
    }

    // ProblemDetail 생성
    private ProblemDetail getProblemDetail(
            final HttpStatus status,
            final Exception e,
            final String message
    ) {
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, message);

        problemDetail.setProperty("exception", e.getClass().getSimpleName());
        problemDetail.setProperty("timestamp", System.currentTimeMillis());

        return problemDetail;
    }
}
