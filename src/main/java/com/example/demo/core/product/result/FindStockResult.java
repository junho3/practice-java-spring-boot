package com.example.demo.core.product.result;

import com.example.demo.core.product.domain.Stock;
import lombok.Getter;

@Getter
public class FindStockResult {

    private final String productCode;
    private final long quantity;

    public FindStockResult(String productCode, long quantity) {
        this.productCode = productCode;
        this.quantity = quantity;
    }

    public static FindStockResult from(Stock stock) {
        return new FindStockResult(
            stock.getProductCode(),
            stock.getQuantity()
        );
    }
}
