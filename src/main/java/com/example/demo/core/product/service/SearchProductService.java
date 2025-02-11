package com.example.demo.core.product.service;

import com.example.demo.config.persistence.ReadTransactional;
import com.example.demo.core.product.domain.Product;
import com.example.demo.core.product.param.SearchProductParam;
import com.example.demo.core.product.result.SearchProductResult;
import com.example.demo.infrastructure.persistence.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@ReadTransactional
@RequiredArgsConstructor
public class SearchProductService {

    private final ProductRepository productRepository;

    public SearchProductResult search(final SearchProductParam param) {
        final Page<Product> products = productRepository.search(param);

        return SearchProductResult.from(products);
    }

    @Cacheable(value = "products", key = "#param")
    public SearchProductResult searchWithCache(final SearchProductParam param) {
        final Page<Product> products = productRepository.search(param);

        return SearchProductResult.from(products);
    }
}
