package com.example.demo.web.v1.product;

import com.example.demo.core.product.service.DecreaseStockService;
import com.example.demo.web.ApiResponse;
import com.example.demo.web.v1.product.request.DecreaseStockRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DecreaseStockController {

    private final DecreaseStockService decreaseStockService;

    public DecreaseStockController(DecreaseStockService decreaseStockService) {
        this.decreaseStockService = decreaseStockService;
    }

    @PostMapping("/v1/product/stock/decrease")
    public ApiResponse<Void> decrease(@RequestBody @Valid DecreaseStockRequest request) {
        decreaseStockService.decrease(request.toParam());

        return ApiResponse.success();
    }
}
