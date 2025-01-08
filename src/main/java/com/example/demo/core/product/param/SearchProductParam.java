package com.example.demo.core.product.param;

import com.example.demo.common.enums.product.ProductStatus;
import org.springframework.data.domain.PageRequest;

public record SearchProductParam(String productName,
                                 Long minProductAmount,
                                 Long maxProductAmount,
                                 ProductStatus productStatus,
                                 PageRequest pageable) {

    public SearchProductParam(String productName,
                              Long minProductAmount,
                              Long maxProductAmount,
                              ProductStatus productStatus,
                              int pageNumber,
                              int pageSize) {
        this(productName, minProductAmount, maxProductAmount, productStatus, PageRequest.of(pageNumber, pageSize));
    }
}
