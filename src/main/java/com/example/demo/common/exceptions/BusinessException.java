package com.example.demo.common.exceptions;

public class BusinessException extends RuntimeException {

    private final BusinessErrorCode businessErrorCode;

    public BusinessException(BusinessErrorCode businessErrorCode) {
        this.businessErrorCode = businessErrorCode;
    }

    public BusinessException(String message, BusinessErrorCode businessErrorCode) {
        super(message);
        this.businessErrorCode = businessErrorCode;
    }

    public BusinessErrorCode getBusinessErrorCode() {
        return businessErrorCode;
    }
}
