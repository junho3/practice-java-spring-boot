package com.example.demo.web.v1.product.request;

import com.example.demo.common.enums.product.ProductStatus;
import com.example.demo.core.product.param.SearchProductParam;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchProductRequest {

    private String productName;

    @PositiveOrZero
    private Long fromProductAmount;

    @PositiveOrZero
    private Long toProductAmount;

    private ProductStatus productStatus;

    @Positive
    private Integer pageNumber;

    @Positive
    private Integer pageSize;

    public SearchProductRequest(
        String productName,
        Long fromProductAmount,
        Long toProductAmount,
        ProductStatus productStatus,
        Integer pageNumber,
        Integer pageSize
    ) {
        this.productName = productName;
        this.fromProductAmount = fromProductAmount;
        this.toProductAmount = toProductAmount;
        this.productStatus = productStatus;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public SearchProductParam toParam() {
        return new SearchProductParam(
            productName,
            fromProductAmount,
            toProductAmount,
            productStatus,
            pageNumber == null ? 0 : pageNumber - 1,
            pageSize == null ? 10 : pageSize
        );
    }
}
