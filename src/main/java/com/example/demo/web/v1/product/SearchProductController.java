package com.example.demo.web.v1.product;

import com.example.demo.core.product.param.SearchProductParam;
import com.example.demo.core.product.service.SearchProductService;
import com.example.demo.web.ApiResponse;
import com.example.demo.web.v1.product.response.FindProductResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SearchProductController {

    private final SearchProductService searchProductService;

    public SearchProductController(SearchProductService searchProductService) {
        this.searchProductService = searchProductService;
    }

    @GetMapping("/v1/product")
    public ApiResponse<List<FindProductResponse>> search() {
        List<FindProductResponse> response = searchProductService.search(
            new SearchProductParam(
                null,
                null,
                null,
                null
            )
        )
            .stream()
            .map(FindProductResponse::from)
            .collect(Collectors.toList());

        return ApiResponse.success(response);
    }
}
