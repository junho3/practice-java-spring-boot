package com.example.demo.core.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderNo {
    @Column(name = "order_no", nullable = false, updatable = false, unique = true)
    private String value;

    public OrderNo(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("OrderNo cannot be null or empty");
        }
        this.value = value;
    }
}
