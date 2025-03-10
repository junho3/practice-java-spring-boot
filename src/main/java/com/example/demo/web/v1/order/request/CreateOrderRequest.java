package com.example.demo.web.v1.order.request;

import com.example.demo.core.order.param.CreateOrderParam;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

public record CreateOrderRequest(@NotNull long memberNo,
                                 @Valid @NotEmpty Set<Product> products) {

    public record Product(@NotEmpty String productCode,
                          @NotEmpty String productName,
                          @Positive long quantity,
                          @Positive BigDecimal productAmount) {

        public CreateOrderParam.Product toParam() {
            return new CreateOrderParam.Product(productCode, productName, quantity, productAmount.longValue());
        }
    }

    public CreateOrderParam toParam() {
        return new CreateOrderParam(
            memberNo,
            products.stream().map(CreateOrderRequest.Product::toParam).collect(Collectors.toSet())
        );
    }
}
