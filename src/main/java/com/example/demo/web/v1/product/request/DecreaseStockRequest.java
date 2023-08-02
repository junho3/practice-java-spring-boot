package com.example.demo.web.v1.product.request;

import com.example.demo.core.product.param.DecreaseStockParam;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class DecreaseStockRequest {

    private final Set<Stock> stocks;

    public DecreaseStockRequest(Set<Stock> stocks) {
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

        public DecreaseStockParam.Stock toParam() {
            return new DecreaseStockParam.Stock(productCode, quantity);
        }
    }

    public DecreaseStockParam toParam() {
        return new DecreaseStockParam(
            stocks.stream().map(Stock::toParam).collect(Collectors.toSet())
        );
    }
}
