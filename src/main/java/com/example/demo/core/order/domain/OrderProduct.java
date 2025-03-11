package com.example.demo.core.order.domain;

import com.example.demo.config.persistence.AuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "order_product")
public class OrderProduct extends AuditEntity {
    @Id
    @Column(name = "order_product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderProductId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Order order;

    @Column(name = "product_code", updatable = false, nullable = false)
    private String productCode;

    @Column(name = "product_name", updatable = false, nullable = false)
    private String productName;

    @Column(name = "quantity", updatable = false, nullable = false)
    private int quantity;

    @Column(name = "product_amount", updatable = false, nullable = false)
    private BigDecimal productAmount;

    public OrderProduct(final Order order,
                        final String productCode,
                        final String productName,
                        final int quantity,
                        final BigDecimal productAmount) {
        this.order = order;
        this.productCode = productCode;
        this.productName = productName;
        this.quantity = quantity;
        this.productAmount = productAmount;
    }
}
