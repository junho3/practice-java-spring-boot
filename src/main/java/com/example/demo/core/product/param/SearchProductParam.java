package com.example.demo.core.product.param;

import com.example.demo.common.enums.product.ProductStatus;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;

@Getter
public class SearchProductParam {

    private final String productName;

    private final Long fromProductAmount;

    private final Long toProductAmount;

    private final ProductStatus productStatus;

    private final PageRequest pageable;

    public SearchProductParam(
        String productName,
        Long fromProductAmount,
        Long toProductAmount,
        ProductStatus productStatus,
        int pageNumber,
        int pageSize
    ) {
        this.productName = productName;
        this.fromProductAmount = fromProductAmount;
        this.toProductAmount = toProductAmount;
        this.productStatus = productStatus;
        this.pageable = PageRequest.of(pageNumber, pageSize);
    }
}
