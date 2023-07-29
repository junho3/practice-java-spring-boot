package com.example.demo.core.product.service;

import com.example.demo.core.product.param.SearchProductParam;
import com.example.demo.core.product.result.FindProductResult;
import com.example.demo.infrastructure.persistence.product.ProductCustomRepository;
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

    public List<FindProductResult> search(SearchProductParam param) {
        return productCustomRepository.search(param)
            .stream()
            .map(FindProductResult::from)
            .collect(Collectors.toList());
    }
}
