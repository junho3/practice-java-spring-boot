package com.example.demo.web.v1.order.response;

import com.example.demo.core.order.result.CreateOrderResult;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

import static com.example.demo.common.constants.DateFormatConstants.ISO_8601;
import static com.example.demo.common.constants.DateFormatConstants.TIMEZONE;

@Getter
public class CreateOrderResponse {

    private final String orderNo;

    private final long memberNo;

    private final String orderName;

    private final long transactionAmount;

    @JsonFormat(pattern = ISO_8601, timezone = TIMEZONE)
    private final LocalDateTime createdAt;

    public CreateOrderResponse(
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

    public static CreateOrderResponse from(CreateOrderResult result) {
        return new CreateOrderResponse(
            result.getOrderNo(),
            result.getMemberNo(),
            result.getOrderName(),
            result.getTransactionAmount(),
            result.getCreatedAt()
        );
    }
}
