package com.example.demo.core.product.service;

import com.example.demo.core.product.domain.Product;
import com.example.demo.core.product.param.SearchProductParam;
import com.example.demo.core.product.result.SearchProductResult;
import com.example.demo.infrastructure.persistence.product.ProductCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchProductService {

    private final ProductCustomRepository productCustomRepository;

    public SearchProductResult search(SearchProductParam param) {
        Page<Product> products = productCustomRepository.search(param);

        return SearchProductResult.from(products);
    }

    @Cacheable(value = "products", key = "#param.productStatus")
    public SearchProductResult searchWithCache(final SearchProductParam param) {
        final Page<Product> products = productCustomRepository.search(param);

        return SearchProductResult.from(products);
    }
}
