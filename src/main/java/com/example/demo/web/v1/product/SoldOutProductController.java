package com.example.demo.web.v1.product;

import com.example.demo.core.product.result.FindProductResult;
import com.example.demo.core.product.service.SoldOutProductService;
import com.example.demo.web.ApiResponse;
import com.example.demo.web.v1.product.response.FindProductResponse;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SoldOutProductController {

    private final SoldOutProductService soldOutProductService;

    public SoldOutProductController(SoldOutProductService soldOutProductService) {
        this.soldOutProductService = soldOutProductService;
    }

    @PutMapping("/v1/product/{productCode}/sold-out")
    public ApiResponse<FindProductResponse> soldOut(@PathVariable("productCode") @NotBlank String productCode) {
        FindProductResult result = soldOutProductService.soldOut(productCode);

        return ApiResponse.success(FindProductResponse.from(result));
    }
}
