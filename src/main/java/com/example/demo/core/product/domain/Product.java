package com.example.demo.core.product.domain;

import com.example.demo.common.enums.product.ProductStatus;
import com.example.demo.common.exceptions.BusinessErrorCode;
import com.example.demo.common.exceptions.BusinessException;
import com.example.demo.config.persistence.AuditEntity;
import com.example.demo.core.stock.domain.Stock;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "product")
public class Product extends AuditEntity {
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(name = "product_code", nullable = false, updatable = false, unique = true)
    private String productCode;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status", nullable = false)
    private ProductStatus productStatus;

    @Column(name = "product_amount", nullable = false)
    private long productAmount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", referencedColumnName = "stock_id", nullable = false, updatable = false)
    private Stock stock;

    public Product(
        String productCode,
        String productName,
        ProductStatus productStatus,
        long productAmount,
        Stock stock
    ) {
        this.productCode = productCode;
        this.productName = productName;
        this.productStatus = productStatus;
        this.productAmount = productAmount;
        this.stock = stock;
    }

    public Product changeSoldOut() {
        if (this.productStatus == ProductStatus.END) {
            throw new BusinessException(BusinessErrorCode.NOT_POSSIBLE_CHANGE_END_TO_SOLD_OUT);
        }

        this.productStatus = ProductStatus.SOLD_OUT;

        return this;
    }

    public Product changeSelling() {
        if (stock.getQuantity() <= 0) {
            throw new BusinessException(BusinessErrorCode.NOT_POSSIBLE_CHANGE_SELLING_AS_STOCK_QUANTITY_EMPTY);
        }

        if (stock.getQuantity() <= stock.getMinLimitQuantity() ) {
            throw new BusinessException(
                BusinessErrorCode.NOT_POSSIBLE_CHANGE_SELLING_AS_STOCK_QUANTITY_LESS_THAN_MIN_LIMIT_STOCK_QUANTITY
            );
        }

        this.productStatus = ProductStatus.SELLING;

        return this;
    }
}
