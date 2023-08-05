package com.example.demo.web.v1.product;

import com.example.demo.core.product.service.IncreaseStockService;
import com.example.demo.web.ApiResponse;
import com.example.demo.web.v1.product.request.IncreaseStockRequest;
import com.example.demo.web.v1.product.response.FindStockResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class IncreaseStockController {

    private final IncreaseStockService increaseStockService;

    public IncreaseStockController(IncreaseStockService increaseStockService) {
        this.increaseStockService = increaseStockService;
    }

    @PostMapping("/v1/product/stock/increase")
    public ApiResponse<List<FindStockResponse>> increase(@RequestBody @Valid IncreaseStockRequest request) {
        List<FindStockResponse> response = increaseStockService.increase(request.toParam())
            .stream()
            .map(FindStockResponse::from)
            .toList();

        return ApiResponse.success(response);
    }
}
