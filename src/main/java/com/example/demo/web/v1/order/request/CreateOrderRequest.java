package com.example.demo.web.v1.order.request;

import com.example.demo.core.order.param.CreateOrderParam;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateOrderRequest {

    @NotNull
    private long memberNo;

    @NotEmpty
    private Set<Product> products;

    @Getter
    public static class Product {

        @NotEmpty
        private String productCode;

        @NotEmpty
        private String productName;

        @Positive
        private long quantity;

        @Positive
        private long productAmount;

        public CreateOrderParam.Product toParam() {
            return new CreateOrderParam.Product(productCode, productName, quantity, productAmount);
        }
    }

    public CreateOrderParam toParam() {
        return new CreateOrderParam(
            memberNo,
            products.stream().map(CreateOrderRequest.Product::toParam).collect(Collectors.toSet())
        );
    }
}
