package com.example.demo.common.exceptions;

public abstract class BusinessException extends RuntimeException {

    private final BusinessErrorCode businessErrorCode;

    protected BusinessException(BusinessErrorCode businessErrorCode) {
        this.businessErrorCode = businessErrorCode;
    }

    protected BusinessException(String message, BusinessErrorCode businessErrorCode) {
        super(message);
        this.businessErrorCode = businessErrorCode;
    }
}
