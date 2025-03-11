package com.example.demo.core.order.param;

import com.example.demo.core.order.domain.Order;
import com.example.demo.core.order.domain.OrderProduct;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

public record CreateOrderParam(long memberNo,
                               @NotNull Set<Product> products) {

    public record Product(@NotEmpty String productCode,
                          @NotEmpty String productName,
                          @Positive long quantity,
                          @NotNull @Positive BigDecimal productAmount) {

        public OrderProduct toEntity() {
            return new OrderProduct(productCode, productName, quantity, productAmount);
        }

        private BigDecimal getTransactionAmount() {
            return productAmount.multiply(BigDecimal.valueOf(quantity));
        }
    }

    public Order toEntity(String orderNo) {
        return new Order(
            orderNo,
            this.memberNo,
            getOrderName(),
            this.products.stream().map(Product::getTransactionAmount).reduce(BigDecimal.ZERO, BigDecimal::add),
            this.products.stream().map(Product::toEntity).collect(Collectors.toSet())
        );
    }

    private String getOrderName() {
        String firstProductName = products.iterator().next().productName;

        if (products.size() > 1) {
            return String.format("%s 외 %d개", firstProductName, products.size() - 1L);
        }

        return firstProductName;
    }
}
