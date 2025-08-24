package com.example.demo.web.v1.stock;

import com.example.demo.core.stock.service.DecreaseStockService;
import com.example.demo.web.ApiResponse;
import com.example.demo.web.v1.stock.request.DecreaseStockRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DecreaseStockController {

    private final DecreaseStockService decreaseStockService;

    @PostMapping("/v1/stocks/decrease")
    public ApiResponse<Void> decrease(@RequestBody @Valid DecreaseStockRequest request) {
        decreaseStockService.decrease(request.toParam());

        return ApiResponse.success();
    }
}
