package com.example.demo.core.product.service;

import com.example.demo.core.product.param.FindProductSalesTop10Param;
import com.example.demo.core.product.result.FindProductSalesResult;
import com.example.demo.infrastructure.persistence.product.ProductSalesRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindProductSalesService {

    private final ProductSalesRepository productSalesRepository;

    @CircuitBreaker(name = "product-sales-top10", fallbackMethod = "fallback")
    public List<FindProductSalesResult> top10(final FindProductSalesTop10Param param) {
        if (param.salesDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException();
        }

        return productSalesRepository.findTop10BySalesDateOrderBySalesQuantityDesc(param.salesDate())
            .stream()
            .map(FindProductSalesResult::of)
            .toList();
    }

    private List<FindProductSalesResult> fallback(final FindProductSalesTop10Param param, final Exception e) {
        log.warn("product-sales-top10 fallback!!");
        return List.of(new FindProductSalesResult("XXXX", "기본상품", param.salesDate(), 10));
    }
}
