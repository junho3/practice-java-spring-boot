package com.example.demo.web.v1.product.response;

import com.example.demo.common.enums.product.ProductStatus;
import com.example.demo.core.product.result.FindProductResult;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FindProductResponse {

    private final String productCode;

    private final String productName;

    private final ProductStatus productStatus;

    private final long productAmount;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    public FindProductResponse(
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

    public static FindProductResponse from(FindProductResult product) {
        return new FindProductResponse(
            product.getProductCode(),
            product.getProductName(),
            product.getProductStatus(),
            product.getProductAmount(),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
}
