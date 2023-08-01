package com.example.demo.core.product.param;

import lombok.Getter;

import java.util.Set;

@Getter
public class DecreaseStockParam {

    private final Set<Stock> stocks;

    public DecreaseStockParam(Set<Stock> stocks) {
        this.stocks = stocks;
    }

    @Getter
    public static class Stock {

        private final String productCode;
        private final long quantity;

        public Stock(String productCode, long quantity) {
            this.productCode = productCode;
            this.quantity = quantity;
        }
    }
}
