package com.example.demo.core.order.domain;

import com.example.demo.config.persistence.AuditEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "orders")
public class Order extends AuditEntity {
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(name = "order_no", nullable = false, updatable = false, unique = true)
    private String orderNo;

    @Column(name = "member_no", nullable = false, updatable = false)
    private long memberNo;

    @Column(name = "order_name", nullable = false, updatable = false)
    private String orderName;

    @Column(name = "transaction_amount", nullable = false, updatable = false)
    private long transactionAmount;

    @OneToMany(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "order_product_id")
    private Set<OrderProduct> products;

    public Order(
        String orderNo,
        long memberNo,
        String orderName,
        long transactionAmount,
        Set<OrderProduct> products
    ) {
        this.orderNo = orderNo;
        this.memberNo = memberNo;
        this.orderName = orderName;
        this.transactionAmount = transactionAmount;
        this.products = products;
    }
}
