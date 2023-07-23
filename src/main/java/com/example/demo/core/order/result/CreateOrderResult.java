package com.example.demo.core.order.result;

import com.example.demo.core.order.domain.Order;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateOrderResult {

    private final String orderNo;

    private final long memberNo;

    private final String orderName;

    private final long transactionAmount;

    private final LocalDateTime createdAt;

    public CreateOrderResult(
        String orderNo,
        long memberNo,
        String orderName,
        long transactionAmount,
        LocalDateTime createdAt
    ) {
        this.orderNo = orderNo;
        this.memberNo = memberNo;
        this.orderName = orderName;
        this.transactionAmount = transactionAmount;
        this.createdAt = createdAt;
    }

    public static CreateOrderResult from(Order order) {
        return new CreateOrderResult(
            order.getOrderNo(),
            order.getMemberNo(),
            order.getOrderName(),
            order.getTransactionAmount(),
            order.getCreatedAt()
        );
    }
}
