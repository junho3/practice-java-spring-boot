package com.example.demo.core.order.domain;

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
@Table(name = "order_product")
public class OrderProduct extends AuditEntity {
    @Id
    @Column(name = "order_product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderProductId;

    @Column(name = "product_code", updatable = false, nullable = false)
    private String productCode;

    @Column(name = "product_name", updatable = false, nullable = false)
    private String productName;

    @Column(name = "quantity", updatable = false, nullable = false)
    private long quantity;

    @Column(name = "product_amount", updatable = false, nullable = false)
    private long productAmount;

    public OrderProduct(String productCode, String productName, long quantity, long productAmount) {
        this.productCode = productCode;
        this.productName = productName;
        this.quantity = quantity;
        this.productAmount = productAmount;
    }
}
