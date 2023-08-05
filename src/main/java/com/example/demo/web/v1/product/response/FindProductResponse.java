package com.example.demo.web.v1.product.response;

import com.example.demo.common.enums.product.ProductStatus;
import com.example.demo.core.product.result.FindProductResult;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

import static com.example.demo.common.constants.DateFormatConstants.ISO_8601;
import static com.example.demo.common.constants.DateFormatConstants.TIMEZONE;

@Getter
public class FindProductResponse {

    private final String productCode;
    private final String productName;
    private final ProductStatus productStatus;
    private final long productAmount;
    @JsonFormat(pattern = ISO_8601, timezone = TIMEZONE)
    private final LocalDateTime createdAt;
    @JsonFormat(pattern = ISO_8601, timezone = TIMEZONE)
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
