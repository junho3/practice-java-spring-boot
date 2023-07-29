package com.example.demo.web.v1.product;

import com.example.demo.core.product.service.CreateProductService;
import com.example.demo.web.ApiResponse;
import com.example.demo.web.v1.product.request.CreateProductRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateProductController {

    private final CreateProductService createProductService;

    public CreateProductController(CreateProductService createProductService) {
        this.createProductService = createProductService;
    }

    @PostMapping("/v1/product")
    public ApiResponse<Void> create(@RequestBody @Valid CreateProductRequest request) {
        createProductService.create(request.toParam());

        return ApiResponse.success();
    }
}
