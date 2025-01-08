package com.example.demo.web.v1.product;

import com.example.demo.core.product.param.FindProductSalesTop10Param;
import com.example.demo.core.product.service.FindProductSalesService;
import com.example.demo.web.ApiResponse;
import com.example.demo.web.v1.product.response.FindProductSalesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FindProductSalesController {

    private final FindProductSalesService findProductSalesService;

    @GetMapping("/v1/products/sales/top10")
    public ApiResponse<List<FindProductSalesResponse>> top10(@RequestParam(required = false) LocalDate salesDate) {
        final List<FindProductSalesResponse> responses = findProductSalesService
            .top10(new FindProductSalesTop10Param(salesDate))
            .stream()
            .map(FindProductSalesResponse::of)
            .toList();

        return ApiResponse.success(responses);
    }
}
