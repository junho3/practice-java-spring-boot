package com.example.demo.core.product.service;

import com.example.demo.core.product.domain.Product;
import com.example.demo.core.product.param.SearchProductParam;
import com.example.demo.core.product.result.SearchProductResult;
import com.example.demo.infrastructure.persistence.product.ProductCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class SearchProductService {

    private final ProductCustomRepository productCustomRepository;

    public SearchProductService(ProductCustomRepository productCustomRepository) {
        this.productCustomRepository = productCustomRepository;
    }

    public SearchProductResult search(SearchProductParam param) {
        Page<Product> products = productCustomRepository.search(param);

        return SearchProductResult.from(products);
    }
}
