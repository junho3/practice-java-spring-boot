package com.example.demo.core.product.domain;

import com.example.demo.common.enums.product.ProductStatus;
import com.example.demo.common.enums.product.ProductType;
import com.example.demo.core.stock.domain.Stock;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@DiscriminatorValue(ProductType.Values.FOOD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FoodProduct extends Product {

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    public FoodProduct(final String productCode,
                       final String productName,
                       final ProductStatus productStatus,
                       final long productAmount,
                       final Stock stock,
                       final LocalDate expirationDate) {
        super(productCode, productName, productStatus, productAmount, stock);
        this.expirationDate = expirationDate;
    }
}
