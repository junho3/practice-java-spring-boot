package com.example.demo.common.enums.product;

import lombok.Getter;

@Getter
public enum ProductType {
    FOOD;

    public static class Values {
        public static final String FOOD = "FOOD";
    }
}
