package com.example.demo.web.v1.product.response;

import com.example.demo.common.enums.product.ProductStatus;
import com.example.demo.core.product.result.SearchProductResult;
import com.example.demo.web.PageResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demo.common.constants.DateFormatConstants.ISO_8601;
import static com.example.demo.common.constants.DateFormatConstants.TIMEZONE;

@Getter
public class SearchProductResponse extends PageResponse {

    private final Set<Product> products;

    public SearchProductResponse(Set<Product> products, long pageNumber, long pageSize, long totalCount) {
        super(pageNumber, pageSize, totalCount);
        this.products = products;
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

        public static Product from(SearchProductResult.Product product) {
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

    public static SearchProductResponse from(SearchProductResult product) {
        return new SearchProductResponse(
            product.getProducts().stream().map(Product::from).collect(Collectors.toSet()),
            product.getPageNumber(),
            product.getPageSize(),
            product.getTotalCount()
        );
    }
}
