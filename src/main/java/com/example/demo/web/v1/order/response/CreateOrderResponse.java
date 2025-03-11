package com.example.demo.web.v1.order.response;

import com.example.demo.core.order.result.CreateOrderResult;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.example.demo.common.constants.DateFormatConstants.ISO_8601;
import static com.example.demo.common.constants.DateFormatConstants.TIMEZONE;

public record CreateOrderResponse(@NotNull String orderNo,
                                  long memberNo,
                                  @NotNull String orderName,
                                  @NotNull BigDecimal transactionAmount,
                                  @NotNull @JsonFormat(pattern = ISO_8601, timezone = TIMEZONE) LocalDateTime createdAt) {

    public static CreateOrderResponse from(final CreateOrderResult result) {
        return new CreateOrderResponse(
            result.orderNo(),
            result.memberNo(),
            result.orderName(),
            result.transactionAmount(),
            result.createdAt()
        );
    }
}
