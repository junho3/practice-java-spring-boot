package com.example.demo.web.v1.product.response;

import com.example.demo.core.product.result.FindProductSalesResult;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record FindProductSalesResponse(@NotNull String productCode,
                                       @NotNull String productName,
                                       @NotNull LocalDate salesDate,
                                       long salesQuantity) {

    public static FindProductSalesResponse of(final FindProductSalesResult result) {
        return new FindProductSalesResponse(
            result.productCode(),
            result.productName(),
            result.salesDate(),
            result.salesQuantity());
    }
}
