package com.example.demo.core.product.domain;

import com.example.demo.common.enums.product.ProductStatus;
import com.example.demo.common.enums.product.ProductType;
import com.example.demo.common.enums.product.VoltType;
import com.example.demo.core.stock.domain.Stock;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue(ProductType.Values.ELECTRONIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ElectronicProduct extends Product {

    @Enumerated(EnumType.STRING)
    @Column(name = "volt_type")
    private VoltType voltType;

    public ElectronicProduct(final String productCode,
                             final String productName,
                             final ProductStatus productStatus,
                             final long productAmount,
                             final Stock stock,
                             final VoltType voltType) {
        super(productCode, productName, productStatus, productAmount, stock);
        this.voltType = voltType;
    }
}
