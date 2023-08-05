package com.example.demo.core.product.service;

import com.example.demo.common.exceptions.BusinessErrorCode;
import com.example.demo.common.exceptions.BusinessException;
import com.example.demo.core.product.domain.Product;
import com.example.demo.core.product.result.FindProductResult;
import com.example.demo.infrastructure.persistence.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class SellProductService {

    private final ProductRepository productRepository;

    public SellProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public FindProductResult sell(String productCode) {
        Product product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new BusinessException(BusinessErrorCode.NOT_FOUND_PRODUCT))
            .changeSelling();

        return FindProductResult.from(product);
    }
}
