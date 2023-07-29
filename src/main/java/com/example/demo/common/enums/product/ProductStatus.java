package com.example.demo.common.enums.product;

public enum ProductStatus {

    READY("판매준비"),
    SELLING("판매"),
    SOLD_OUT("품절")
    ;

    private final String description;

    ProductStatus(final String description) {
        this.description = description;
    }
}
