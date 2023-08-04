package com.example.demo.web.v1.product.response;

import com.example.demo.core.product.result.FindStockResult;
import lombok.Getter;

@Getter
public class FindStockResponse {

    private final String productCode;
    private final long quantity;
    private final long minLimitQuantity;

    public FindStockResponse(String productCode, long quantity, long minLimitQuantity) {
        this.productCode = productCode;
        this.quantity = quantity;
        this.minLimitQuantity = minLimitQuantity;
    }

    public static FindStockResponse from(FindStockResult stock) {
        return new FindStockResponse(
            stock.getProductCode(),
            stock.getQuantity(),
            stock.getMinLimitQuantity()
        );
    }
}
