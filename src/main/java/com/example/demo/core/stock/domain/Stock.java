package com.example.demo.core.stock.domain;

import com.example.demo.common.exceptions.BusinessErrorCode;
import com.example.demo.common.exceptions.BusinessException;
import com.example.demo.config.persistence.AuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "stock")
public class Stock extends AuditEntity {
    @Id
    @Column(name = "stock_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;

    @Column(name = "product_code", nullable = false, updatable = false, unique = true)
    private String productCode;

    @Column(name = "quantity", nullable = false)
    private long quantity;

    @Column(name = "min_limit_quantity", nullable = false)
    private long minLimitQuantity;

    public Stock(String productCode, long quantity, long minLimitQuantity) {
        this.productCode = productCode;
        this.quantity = quantity;
        this.minLimitQuantity = minLimitQuantity;
    }

    public Stock decrease(long quantity) {
        long decreasedQuantity = this.quantity - quantity;

        if (decreasedQuantity < this.minLimitQuantity) {
            throw new BusinessException(BusinessErrorCode.INVALID_STOCK_QUANTITY);
        }

        if (decreasedQuantity < 0) {
            throw new BusinessException(BusinessErrorCode.INVALID_STOCK_QUANTITY);
        }

        this.quantity = decreasedQuantity;

        return this;
    }

    public Stock increase(long quantity) {
        this.quantity = this.quantity + quantity;

        return this;
    }

    public boolean isLimitQuantity() {
        if (this.quantity <= this.minLimitQuantity) {
            return true;
        }

        return this.quantity == 0;
    }
}
