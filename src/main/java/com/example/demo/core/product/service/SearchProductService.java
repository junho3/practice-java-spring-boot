package com.example.demo.core.product.service;

import com.example.demo.config.persistence.ReadTransactional;
import com.example.demo.core.product.domain.Product;
import com.example.demo.core.product.param.SearchProductParam;
import com.example.demo.core.product.result.SearchProductResult;
import com.example.demo.infrastructure.persistence.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ReadTransactional
@RequiredArgsConstructor
public class SearchProductService {

    private final ProductRepository productRepository;

    public SearchProductResult search(final SearchProductParam param) {
        final Page<Product> products = productRepository.search(param);

        log.info("search");

        return SearchProductResult.from(products);
    }

    @Cacheable(value = "products", key = "#param")
    public SearchProductResult searchWithCache(final SearchProductParam param) {
        final Page<Product> products = productRepository.search(param);

        log.info("searchWithCache");

        return SearchProductResult.from(products);
    }

    @Cacheable(
        value = "products",
        keyGenerator = "searchProductKeyGenerator"
    )
    public SearchProductResult searchWithCacheByKeyGenerator(final SearchProductParam param) {
        final Page<Product> products = productRepository.search(param);

        log.info("searchWithCacheByKeyGenerator");

        return SearchProductResult.from(products);
    }

    @Cacheable(
        value = "products",
        keyGenerator = "searchProductKeyGenerator",
        condition = "#param.pageable.pageNumber == 0"
    )
    public SearchProductResult searchWithCacheAsFirstPage(final SearchProductParam param) {
        final Page<Product> products = productRepository.search(param);

        log.info("searchWithCacheAsFirstPage");

        return SearchProductResult.from(products);
    }

    @Cacheable(
        value = "products",
        keyGenerator = "searchProductKeyGenerator",
        unless = "#result.products.isEmpty()"
    )
    public SearchProductResult searchWithCacheAsNotEmpty(final SearchProductParam param) {
        final Page<Product> products = productRepository.search(param);

        log.info("searchWithCacheAsNotEmpty");

        return SearchProductResult.from(products);
    }

    @CachePut(
        value = "products",
        keyGenerator = "searchProductKeyGenerator",
        condition = "#param.pageable.pageNumber == 0",
        unless = "#result.products.isEmpty()"
    )
    public SearchProductResult refreshSearchCache(final SearchProductParam param) {
        final Page<Product> products = productRepository.search(param);

        log.info("refreshSearchCache");

        return SearchProductResult.from(products);
    }
}
