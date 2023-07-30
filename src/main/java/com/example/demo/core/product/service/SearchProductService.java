package com.example.demo.core.product.service;

import com.example.demo.core.product.domain.Product;
import com.example.demo.core.product.param.SearchProductParam;
import com.example.demo.core.product.result.FindProductResult;
import com.example.demo.infrastructure.persistence.product.ProductCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class SearchProductService {

    private final ProductCustomRepository productCustomRepository;

    public SearchProductService(ProductCustomRepository productCustomRepository) {
        this.productCustomRepository = productCustomRepository;
    }

    public FindProductResult search(SearchProductParam param) {
        Page<Product> products = productCustomRepository.search(param);

        return FindProductResult.from(products);
    }
}
