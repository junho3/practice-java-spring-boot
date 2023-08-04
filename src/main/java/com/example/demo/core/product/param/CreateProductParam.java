package com.example.demo.core.product.param;

import com.example.demo.common.enums.product.ProductStatus;
import com.example.demo.core.product.domain.Product;
import com.example.demo.core.product.domain.Stock;
import lombok.Getter;

@Getter
public class CreateProductParam {

    private final String productCode;
    private final String productName;
    private final long productAmount;
    private final long quantity;
    private final long minLimitQuantity;

    public CreateProductParam(
        String productCode,
        String productName,
        long productAmount,
        long quantity,
        long minLimitQuantity
    ) {
        this.productCode = productCode;
        this.productName = productName;
        this.productAmount = productAmount;
        this.quantity = quantity;
        this.minLimitQuantity = minLimitQuantity;
    }

    public Stock toStockEntity() {
        return new Stock(productCode, quantity, minLimitQuantity);
    }

    public Product toProductEntity(Stock stock) {
        return new Product(
            productCode,
            productName,
            ProductStatus.READY,
            productAmount,
            stock
        );
    }
}
