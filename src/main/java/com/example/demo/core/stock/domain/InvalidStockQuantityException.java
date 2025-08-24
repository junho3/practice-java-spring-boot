package com.example.demo.core.stock.domain;

import com.example.demo.common.exceptions.BusinessException;

import java.io.Serial;

import static com.example.demo.common.exceptions.BusinessErrorCode.INVALID_STOCK_QUANTITY;

public class InvalidStockQuantityException extends BusinessException {

    @Serial
    private static final long serialVersionUID = -8163366324230620494L;

    public InvalidStockQuantityException() {
        super(INVALID_STOCK_QUANTITY);
    }
}
