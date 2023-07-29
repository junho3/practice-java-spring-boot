package com.example.demo.web.v1.product.response;

import com.example.demo.common.enums.product.ProductStatus;
import com.example.demo.core.product.result.FindProductResult;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demo.common.constants.DateFormatConstants.ISO_8601;
import static com.example.demo.common.constants.DateFormatConstants.TIMEZONE;

@Getter
public class FindProductResponse {

    private final Set<Product> products;
    private final Pageable pageable;

    public FindProductResponse(Set<Product> products, Pageable pageable) {
        this.products = products;
        this.pageable = pageable;
    }

    @Getter
    private static class Product {
        private final String productCode;

        private final String productName;

        private final ProductStatus productStatus;

        private final long productAmount;

        @JsonFormat(pattern = ISO_8601, timezone = TIMEZONE)
        private final LocalDateTime createdAt;

        @JsonFormat(pattern = ISO_8601, timezone = TIMEZONE)
        private final LocalDateTime updatedAt;

        private Product(
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

        public static Product from(FindProductResult.Product product) {
            return new Product(
                product.getProductCode(),
                product.getProductName(),
                product.getProductStatus(),
                product.getProductAmount(),
                product.getCreatedAt(),
                product.getUpdatedAt()
            );
        }
    }

    public static FindProductResponse from(FindProductResult product) {
        return new FindProductResponse(
            product.getProducts().stream().map(Product::from).collect(Collectors.toSet()),
            product.getPageable()
        );
    }
}
