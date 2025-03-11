package com.example.demo.core.order.param;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Set;

public record CreateOrderParam(long memberNo,
                               @NotNull Set<Product> products) {

    public record Product(@NotEmpty String productCode,
                          @NotEmpty String productName,
                          @Positive int quantity,
                          @NotNull @Positive BigDecimal productAmount) {

        public BigDecimal getTransactionAmount() {
            return productAmount.multiply(BigDecimal.valueOf(quantity));
        }
    }

    public String getOrderName() {
        final String firstProductName = products.iterator().next().productName;

        if (products.size() > 1) {
            return String.format("%s 외 %d개", firstProductName, products.size() - 1L);
        }

        return firstProductName;
    }

    public BigDecimal getTransactionAmount() {
        return products().stream()
            .map(CreateOrderParam.Product::getTransactionAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
