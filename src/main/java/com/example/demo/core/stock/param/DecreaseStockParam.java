package com.example.demo.core.stock.param;

import java.util.Objects;
import java.util.Set;

public record DecreaseStockParam(Set<Stock> stocks) {

    public record Stock(
        String productCode,
        long quantity
    ) {
        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Stock stock = (Stock) o;
            return Objects.equals(productCode, stock.productCode);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(productCode);
        }
    }
}
