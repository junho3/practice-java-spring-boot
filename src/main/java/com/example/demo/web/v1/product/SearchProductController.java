package com.example.demo.web.v1.product;

import com.example.demo.core.product.result.FindProductResult;
import com.example.demo.core.product.service.SearchProductService;
import com.example.demo.web.ApiResponse;
import com.example.demo.web.v1.product.request.SearchProductRequest;
import com.example.demo.web.v1.product.response.FindProductResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchProductController {

    private final SearchProductService searchProductService;

    public SearchProductController(SearchProductService searchProductService) {
        this.searchProductService = searchProductService;
    }

    @GetMapping("/v1/product")
    public ApiResponse<FindProductResponse> search(@ModelAttribute @Valid SearchProductRequest request) {
        FindProductResult result = searchProductService.search(request.toParam());

        return ApiResponse.success(FindProductResponse.from(result));
    }
}
