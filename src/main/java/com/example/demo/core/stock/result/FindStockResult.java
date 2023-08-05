package com.example.demo.core.stock.result;

import com.example.demo.core.stock.domain.Stock;
import lombok.Getter;

@Getter
public class FindStockResult {

    private final String productCode;
    private final long quantity;
    private final long minLimitQuantity;

    public FindStockResult(String productCode, long quantity, long minLimitQuantity) {
        this.productCode = productCode;
        this.quantity = quantity;
        this.minLimitQuantity = minLimitQuantity;
    }

    public static FindStockResult from(Stock stock) {
        return new FindStockResult(
            stock.getProductCode(),
            stock.getQuantity(),
            stock.getMinLimitQuantity()
        );
    }
}
