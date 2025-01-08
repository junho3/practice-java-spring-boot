package com.example.demo.web.v1.product;

import com.example.demo.core.product.result.SearchProductResult;
import com.example.demo.core.product.service.SearchProductService;
import com.example.demo.web.ApiResponse;
import com.example.demo.web.v1.product.request.SearchProductRequest;
import com.example.demo.web.v1.product.response.SearchProductResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchProductController {

    private final SearchProductService searchProductService;

    public SearchProductController(SearchProductService searchProductService) {
        this.searchProductService = searchProductService;
    }

    @GetMapping("/v1/products")
    public ApiResponse<SearchProductResponse> search(@RequestParam(required = false, defaultValue = "true") boolean cache,
                                                     @ModelAttribute @Valid SearchProductRequest request) {
        final SearchProductResult result = cache
            ? searchProductService.searchWithCache(request.toParam())
            : searchProductService.search(request.toParam());

        return ApiResponse.success(SearchProductResponse.from(result));
    }
}
