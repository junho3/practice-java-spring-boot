package com.example.demo.core.product.result;

import com.example.demo.common.enums.product.ProductStatus;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class FindProductResult {

    private final Set<Product> products;

    private final Pageable pageable;

    public FindProductResult(Set<Product> products, Pageable pageable) {
        this.products = products;
        this.pageable = pageable;
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


    public static FindProductResult from(Page<com.example.demo.core.product.domain.Product> products) {
        return new FindProductResult(
            products.getContent().stream().map(Product::from).collect(Collectors.toSet()),
            products.getPageable()
        );
    }
}
