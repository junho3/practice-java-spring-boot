package com.example.demo.common.enums.product;

public enum ProductStatus {

    READY("판매준비"),
    SELLING("판매중"),
    SOLD_OUT("품절"),
    END("판매종료")
    ;

    private final String description;

    ProductStatus(final String description) {
        this.description = description;
    }
}
