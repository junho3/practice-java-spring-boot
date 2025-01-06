package com.example.demo.core.product.result;

import com.example.demo.core.product.domain.ProductSalesRanking;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record FindProductSalesRankingResult(@NotNull String productCode,
                                            @NotNull String productName,
                                            @NotNull LocalDate salesDate,
                                            long salesQuantity) {

    public static FindProductSalesRankingResult of(final ProductSalesRanking productSalesRanking) {
        return new FindProductSalesRankingResult(
            productSalesRanking.getProductCode(),
            productSalesRanking.getProductName(),
            productSalesRanking.getSalesDate(),
            productSalesRanking.getSalesQuantity());
    }
}
