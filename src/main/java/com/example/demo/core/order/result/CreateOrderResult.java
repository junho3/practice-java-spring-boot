package com.example.demo.core.order.result;

import com.example.demo.core.order.domain.Order;
import com.example.demo.core.order.domain.OrderProduct;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CreateOrderResult(@NotEmpty String orderNo,
                                long memberNo,
                                @NotEmpty String orderName,
                                @NotNull BigDecimal transactionAmount,
                                @NotNull LocalDateTime createdAt,
                                @NotNull List<Product> products) {

    public record Product(@NotEmpty String productCode,
                          @NotEmpty String productName,
                          int quantity,
                          @NotNull BigDecimal productAmount) {

        public static Product from(final OrderProduct orderProduct) {
            return new CreateOrderResult.Product(
                orderProduct.getProductCode(),
                orderProduct.getProductName(),
                orderProduct.getQuantity(),
                orderProduct.getProductAmount()
            );
        }
    }

    public static CreateOrderResult from(final Order order) {
        return new CreateOrderResult(
            order.getOrderNo(),
            order.getMemberNo(),
            order.getOrderName(),
            order.getTransactionAmount(),
            order.getCreatedAt(),
            order.getProducts().stream().map(Product::from).toList());
    }
}
