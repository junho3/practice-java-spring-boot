package com.example.demo.web.v1.product.request;

import com.example.demo.core.product.param.CreateProductParam;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateProductRequest {

    @NotNull
    private String productCode;

    @NotNull
    private String productName;

    @Positive
    private long productAmount;

    @Positive
    private long quantity;

    @PositiveOrZero
    private long minLimitQuantity;

    public CreateProductParam toParam() {
        return new CreateProductParam(
            productCode,
            productName,
            productAmount,
            quantity,
            minLimitQuantity
        );
    }
}
