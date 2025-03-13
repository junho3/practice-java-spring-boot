package com.example.demo.core.order.domain;

import com.example.demo.config.persistence.AuditEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "orders")
public class Order extends AuditEntity {
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Embedded
    private OrderNo orderNo;

    @Column(name = "member_no", nullable = false, updatable = false)
    private long memberNo;

    @Column(name = "order_name", nullable = false, updatable = false)
    private String orderName;

    @Column(name = "transaction_amount", nullable = false, updatable = false)
    private BigDecimal transactionAmount;

    @OneToMany(fetch = LAZY, cascade = {CascadeType.PERSIST}, mappedBy = "order")
    private List<OrderProduct> products = new LinkedList<>();

    public Order(OrderNo orderNo,
                 long memberNo,
                 String orderName,
                 BigDecimal transactionAmount) {
        this.orderNo = orderNo;
        this.memberNo = memberNo;
        this.orderName = orderName;
        this.transactionAmount = transactionAmount;
    }

    public Order addProducts(final List<OrderProduct> orderProducts) {
        orderProducts.forEach(orderProduct -> {
            orderProduct.setOrder(this);
            products.add(orderProduct);
        });

        return this;
    }
}
