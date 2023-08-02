package com.example.demo.core.product.result;

import com.example.demo.core.product.domain.Stock;
import lombok.Getter;

@Getter
public class IncreaseStockResult {

    private final String productCode;
    private final long quantity;

    public IncreaseStockResult(String productCode, long quantity) {
        this.productCode = productCode;
        this.quantity = quantity;
    }

    public static IncreaseStockResult from(Stock stock) {
        return new IncreaseStockResult(
            stock.getProductCode(),
            stock.getQuantity()
        );
    }
}
