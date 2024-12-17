package com.example.demo.infrastructure.kafka.product;

import lombok.Builder;

@Builder
public record CreateProductPayload(String productCode,
                                   String productName,
                                   long productAmount) {
}
