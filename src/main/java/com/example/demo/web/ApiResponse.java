package com.example.demo.web;

import lombok.Getter;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Getter
public class ApiResponse<T> {

    private final boolean success;

    private final String code;

    private final String message;

    private T data;

    public ApiResponse(Boolean success, String code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public ApiResponse(Boolean success, String code, String message, T data) {
        this(success, code, message);
        this.data = data;
    }

    public ApiResponse(Boolean success, T data) {
        this(success, null, null);
        this.data = data;
    }

    public ApiResponse(Boolean success) {
        this(success, null, null);
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
            "success=" + success +
            ", code='" + code + '\'' +
            ", message='" + message + '\'' +
            ", data=" + data +
            '}';
    }

    public static ApiResponse<Void> fail(String code, String message) {
        return new ApiResponse<>(FALSE, code, message, null);
    }

    public static <D> ApiResponse<D> success(D data) {
        return new ApiResponse<>(TRUE, data);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(TRUE);
    }
}

