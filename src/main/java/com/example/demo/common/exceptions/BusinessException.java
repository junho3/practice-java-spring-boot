package com.example.demo.common.exceptions;

import lombok.Getter;

import java.io.Serial;

@Getter
public class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -7796732682612229338L;
    private final BusinessErrorCode businessErrorCode;

    public BusinessException(BusinessErrorCode businessErrorCode) {
        this.businessErrorCode = businessErrorCode;
    }

    public BusinessException(String message, BusinessErrorCode businessErrorCode) {
        super(message);
        this.businessErrorCode = businessErrorCode;
    }
}
