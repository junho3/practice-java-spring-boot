package com.example.demo.core.order.param;

import com.example.demo.core.order.domain.Order;
import com.example.demo.core.order.domain.OrderProduct;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class CreateOrderParam {

    private final long memberNo;

    private final Set<Product> products;

    @Getter
    public static class Product {

        private final String productCode;

        private final String productName;

        private final long quantity;

        private final long productAmount;

        public Product(String productCode, String productName, long quantity, long productAmount) {
            this.productCode = productCode;
            this.productName = productName;
            this.quantity = quantity;
            this.productAmount = productAmount;
        }

        public OrderProduct toEntity() {
            return new OrderProduct(productCode, productName, quantity, productAmount);
        }

        private Long getTransactionAmount() {
            return productAmount * quantity;
        }
    }

    public CreateOrderParam(long memberNo, Set<Product> products) {
        this.memberNo = memberNo;
        this.products = products;
    }

    public Order toEntity(String orderNo) {
        return new Order(
            orderNo,
            this.memberNo,
            getOrderName(),
            this.products.stream().mapToLong(Product::getTransactionAmount).sum(),
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
