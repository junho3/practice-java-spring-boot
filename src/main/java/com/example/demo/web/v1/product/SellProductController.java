package com.example.demo.web.v1.product;

import com.example.demo.core.product.result.FindProductResult;
import com.example.demo.core.product.service.SellProductService;
import com.example.demo.web.ApiResponse;
import com.example.demo.web.v1.product.response.FindProductResponse;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SellProductController {

    private final SellProductService sellProductService;

    public SellProductController(SellProductService sellProductService) {
        this.sellProductService = sellProductService;
    }

    @PutMapping("/v1/products/{productCode}/sell")
    public ApiResponse<FindProductResponse> sell(@PathVariable("productCode") @NotBlank String productCode) {
        FindProductResult result = sellProductService.sell(productCode);

        return ApiResponse.success(FindProductResponse.from(result));
    }
}
