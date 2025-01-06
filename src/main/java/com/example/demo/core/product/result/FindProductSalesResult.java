package com.example.demo.core.product.result;

import com.example.demo.core.product.domain.ProductSales;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record FindProductSalesResult(@NotNull String productCode,
                                     @NotNull String productName,
                                     @NotNull LocalDate salesDate,
                                     long salesQuantity) {

    public static FindProductSalesResult of(final ProductSales productSales) {
        return new FindProductSalesResult(
            productSales.getProductCode(),
            productSales.getProductName(),
            productSales.getSalesDate(),
            productSales.getSalesQuantity());
    }
}
