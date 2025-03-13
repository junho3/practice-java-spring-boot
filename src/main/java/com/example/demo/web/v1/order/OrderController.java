package com.example.demo.web.v1.order;

import com.example.demo.core.order.domain.OrderNo;
import com.example.demo.core.order.result.OrderAggregateResult;
import com.example.demo.core.order.service.CreateOrderService;
import com.example.demo.core.order.service.FindOrderService;
import com.example.demo.web.ApiResponse;
import com.example.demo.web.v1.order.request.CreateOrderRequest;
import com.example.demo.web.v1.order.response.CreateOrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderService createOrderService;
    private final FindOrderService findOrderService;

    @PostMapping
    public ApiResponse<CreateOrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        final OrderAggregateResult result = createOrderService.create(request.toParam());

        return ApiResponse.success(CreateOrderResponse.from(result));
    }

    @GetMapping("/{orderNo}")
    public ApiResponse<CreateOrderResponse> find(@Valid @PathVariable String orderNo) {
        final OrderAggregateResult result = findOrderService.findAggregate(new OrderNo(orderNo));

        return ApiResponse.success(CreateOrderResponse.from(result));
    }
}
