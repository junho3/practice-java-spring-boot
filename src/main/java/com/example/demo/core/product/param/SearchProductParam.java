package com.example.demo.core.product.param;

import com.example.demo.common.enums.product.ProductStatus;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;

@Getter
public class SearchProductParam {

    private final String productName;

    private final Long minProductAmount;

    private final Long maxProductAmount;

    private final ProductStatus productStatus;

    private final PageRequest pageable;

    public SearchProductParam(
        String productName,
        Long minProductAmount,
        Long maxProductAmount,
        ProductStatus productStatus,
        int pageNumber,
        int pageSize
    ) {
        this.productName = productName;
        this.minProductAmount = minProductAmount;
        this.maxProductAmount = maxProductAmount;
        this.productStatus = productStatus;
        this.pageable = PageRequest.of(pageNumber, pageSize);
    }
}
