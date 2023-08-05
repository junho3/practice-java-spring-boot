package com.example.demo.web.v1.product.request;

import com.example.demo.core.stock.param.IncreaseStockParam;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IncreaseStockRequest {

    @NotEmpty
    private Set<Stock> stocks;

    @Getter
    public static class Stock {

        private final String productCode;
        private final long quantity;

        public Stock(String productCode, long quantity) {
            this.productCode = productCode;
            this.quantity = quantity;
        }

        public IncreaseStockParam.Stock toParam() {
            return new IncreaseStockParam.Stock(productCode, quantity);
        }
    }

    public IncreaseStockParam toParam() {
        return new IncreaseStockParam(
            stocks.stream().map(Stock::toParam).collect(Collectors.toSet())
        );
    }
}
