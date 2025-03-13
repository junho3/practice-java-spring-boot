package com.example.demo.web.v1.order;

import com.example.demo.core.order.result.OrderAggregateResult;
import com.example.demo.core.order.service.CreateOrderService;
import com.example.demo.web.ApiResponse;
import com.example.demo.web.v1.order.request.CreateOrderRequest;
import com.example.demo.web.v1.order.response.CreateOrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderService createOrderService;

    @PostMapping
    public ApiResponse<CreateOrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        final OrderAggregateResult result = createOrderService.create(request.toParam());

        return ApiResponse.success(CreateOrderResponse.from(result));
    }
}
