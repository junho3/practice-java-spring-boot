package com.example.demo.core.product.result;

import com.example.demo.common.enums.product.ProductStatus;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class SearchProductResult {

    private final Set<Product> products;
    private final long pageNumber;
    private final long pageSize;
    private final long totalCount;

    public SearchProductResult(Set<Product> products, long pageNumber, long pageSize, long totalCount) {
        this.products = products;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
    }

    @Getter
    public static class Product {

        private final String productCode;
        private final String productName;
        private final ProductStatus productStatus;
        private final long productAmount;
        private final LocalDateTime createdAt;
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

        public static Product from(com.example.demo.core.product.domain.Product product) {
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

    public static SearchProductResult from(Page<com.example.demo.core.product.domain.Product> products) {
        return new SearchProductResult(
            products.getContent().stream().map(Product::from).collect(Collectors.toSet()),
            products.getPageable().getPageNumber(),
            products.getPageable().getPageSize(),
            products.getTotalElements()
        );
    }
}
