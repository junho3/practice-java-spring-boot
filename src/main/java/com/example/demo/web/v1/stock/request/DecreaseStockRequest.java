package com.example.demo.web.v1.stock.request;

import com.example.demo.core.stock.param.DecreaseStockParam;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.stream.Collectors;

public record DecreaseStockRequest(@Valid @Size(min = 1) Set<Stock> stocks) {

    public record Stock(
        @NotNull @NotEmpty String productCode,
        @Positive long quantity
    ) {
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
