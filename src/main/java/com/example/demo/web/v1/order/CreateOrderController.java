package com.example.demo.web.v1.order;

import com.example.demo.core.order.result.CreateOrderResult;
import com.example.demo.core.order.service.CreateOrderService;
import com.example.demo.web.ApiResponse;
import com.example.demo.web.v1.order.request.CreateOrderRequest;
import com.example.demo.web.v1.order.response.CreateOrderResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateOrderController {

    private final CreateOrderService createOrderService;

    public CreateOrderController(CreateOrderService createOrderService) {
        this.createOrderService = createOrderService;
    }

    @PostMapping("/v1/order")
    public ApiResponse<CreateOrderResponse> create(@RequestBody @Valid CreateOrderRequest request) {
        CreateOrderResult result = createOrderService.create(request.toParam());

        return ApiResponse.success(CreateOrderResponse.from(result));
    }
}
