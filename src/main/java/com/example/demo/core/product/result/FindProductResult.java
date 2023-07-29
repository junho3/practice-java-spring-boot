package com.example.demo.core.product.result;

import com.example.demo.common.enums.product.ProductStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FindProductResult {

    private final String productCode;

    private final String productName;

    private final ProductStatus productStatus;

    private final long productAmount;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    public FindProductResult(
        String productCode,
        String productName,
        ProductStatus productStatus,
        long productAmount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        this.productCode = productCode;
        this.productName = productName;
        this.productStatus = productStatus;
        this.productAmount = productAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
