package com.example.demo.common.enums.product;

public enum ProductStatus {

    SELLING("판매"),
    SOLD_OUT("품절")
    ;

    private final String description;

    ProductStatus(final String description) {
        this.description = description;
    }
}
