package com.example.demo.web.v1.stock;

import com.example.demo.core.stock.service.IncreaseStockService;
import com.example.demo.web.ApiResponse;
import com.example.demo.web.v1.stock.request.IncreaseStockRequest;
import com.example.demo.web.v1.stock.response.FindStockResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IncreaseStockController {

    private final IncreaseStockService increaseStockService;

    public IncreaseStockController(IncreaseStockService increaseStockService) {
        this.increaseStockService = increaseStockService;
    }

    @PostMapping("/v1/stock/increase")
    public ApiResponse<List<FindStockResponse>> increase(@RequestBody @Valid IncreaseStockRequest request) {
        List<FindStockResponse> response = increaseStockService.increase(request.toParam())
            .stream()
            .map(FindStockResponse::from)
            .toList();

        return ApiResponse.success(response);
    }
}
