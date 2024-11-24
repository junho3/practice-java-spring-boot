package com.example.demo.common.enums.product;

import lombok.Getter;

@Getter
public enum ProductType {
    FOOD, ELECTRONIC;

    public static class Values {
        public static final String FOOD = "FOOD";
        public static final String ELECTRONIC = "ELECTRONIC";
    }
}
