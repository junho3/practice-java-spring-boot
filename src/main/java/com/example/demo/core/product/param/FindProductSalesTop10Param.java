package com.example.demo.core.product.param;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record FindProductSalesTop10Param(@NotNull LocalDate salesDate) {
    public FindProductSalesTop10Param {
        if (salesDate == null) {
            salesDate = LocalDate.now();
        }
    }
}
