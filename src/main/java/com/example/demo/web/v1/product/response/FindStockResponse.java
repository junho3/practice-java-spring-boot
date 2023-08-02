package com.example.demo.web.v1.product.response;

import com.example.demo.core.product.result.FindStockResult;
import lombok.Getter;

@Getter
public class FindStockResponse {

    private final String productCode;
    private final long quantity;

    public FindStockResponse(String productCode, long quantity) {
        this.productCode = productCode;
        this.quantity = quantity;
    }

    public static FindStockResponse from(FindStockResult stock) {
        return new FindStockResponse(
            stock.getProductCode(),
            stock.getQuantity()
        );
    }
}
