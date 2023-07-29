package com.example.demo.web.v1.product.request;

import com.example.demo.common.enums.product.ProductStatus;
import com.example.demo.core.product.param.SearchProductParam;
import com.example.demo.web.PageRequest;
import jakarta.validation.constraints.PositiveOrZero;

public class SearchProductRequest extends PageRequest {

    private final String productName;

    @PositiveOrZero
    private final Long fromProductAmount;

    @PositiveOrZero
    private final Long toProductAmount;

    private final ProductStatus productStatus;

    public SearchProductRequest(
        String productName,
        Long fromProductAmount,
        Long toProductAmount,
        ProductStatus productStatus,
        Integer pageNumber,
        Integer pageSize
    ) {
        super(pageNumber, pageSize);
        this.productName = productName;
        this.fromProductAmount = fromProductAmount;
        this.toProductAmount = toProductAmount;
        this.productStatus = productStatus;
    }

    public SearchProductParam toParam() {
        return new SearchProductParam(
            productName.trim(),
            fromProductAmount,
            toProductAmount,
            productStatus,
            getPageNumber(),
            getPageSize()
        );
    }
}
