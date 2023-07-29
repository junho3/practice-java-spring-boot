package com.example.demo.core.product.param;

import com.example.demo.common.enums.product.ProductStatus;
import lombok.Getter;

@Getter
public class SearchProductParam {

    private final String productName;

    private final Long fromProductAmount;

    private final Long toProductAmount;

    private final ProductStatus productStatus;

    public SearchProductParam(
        String productName,
        Long fromProductAmount,
        Long toProductAmount,
        ProductStatus productStatus
    ) {
        this.productName = productName;
        this.fromProductAmount = fromProductAmount;
        this.toProductAmount = toProductAmount;
        this.productStatus = productStatus;
    }
}
