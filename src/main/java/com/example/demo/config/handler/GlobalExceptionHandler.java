package com.example.demo.config.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String DEFAULT_INTERNAL_SERVER_ERR_MESSAGE = "알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.";
    private static final String DEFAULT_BAD_REQUEST_ERR_MESSAGE = "잘못된 요청입니다. 요청내용을 확인하세요.";

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handle(Exception exception) {
        log.error(exception.getMessage(), exception);

        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(INTERNAL_SERVER_ERROR, DEFAULT_INTERNAL_SERVER_ERR_MESSAGE);
        problemDetail.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(problemDetail);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), getErrorsDetails(ex));
        problemDetail.setType(URI.create("http://localhost:8080/errors/bad-request"));
        problemDetail.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(status.value()).body(problemDetail);
    }

    private String getErrorsDetails(MethodArgumentNotValidException ex) {
        return Optional.of(ex.getDetailMessageArguments())
            .map(args -> Arrays.stream(args)
                .filter(msg -> !ObjectUtils.isEmpty(msg))
                .reduce("Please make sure to provide a valid request, ", (a, b) -> a + " " + b)
            )
            .orElse("").toString();
    }
}
