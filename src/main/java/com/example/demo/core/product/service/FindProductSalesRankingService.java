package com.example.demo.core.product.service;

import com.example.demo.core.product.domain.ProductSales;
import com.example.demo.core.product.param.FindProductSalesTop10Param;
import com.example.demo.core.product.result.FindProductSalesResult;
import com.example.demo.infrastructure.persistence.product.ProductSalesRepository;
import com.example.demo.infrastructure.persistence.product.ProductSalesRankingRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindProductSalesRankingService {

    private final ProductSalesRankingRepository productSalesRankingRepository;
    private final ProductSalesRepository productSalesRepository;

    @CircuitBreaker(name = "product-sales-ranking-top10", fallbackMethod = "fallback")
    public List<FindProductSalesResult> top10(final FindProductSalesTop10Param param) {
        final List<String> productCodes = productSalesRankingRepository.findTop10(param.salesDate());

        if (productCodes.isEmpty()) {
            throw new IllegalStateException("Redis에 랭킹 데이터가 존재하지 않습니다.");
        }

        final Map<String, ProductSales> productSalesMap = findProductSalesMapByProductCodes(param.salesDate(), productCodes);

        return productCodes.stream()
                .filter(productSalesMap::containsKey)
                .map(productSalesMap::get)
                .map(FindProductSalesResult::of)
                .toList();
    }

    private Map<String, ProductSales> findProductSalesMapByProductCodes(final LocalDate salesDate,
                                                                        final List<String> productCodes) {
        return productSalesRepository
                .findBySalesDateAndProductCodeIn(salesDate, productCodes)
                .stream()
                .collect(Collectors.toMap(ProductSales::getProductCode, Function.identity()));
    }

    private List<FindProductSalesResult> fallback(final FindProductSalesTop10Param param, final Exception e) {
        log.warn("Redis CircuitBreaker fallback 실행. reason={}", e.getMessage());

        return productSalesRepository.findTop10BySalesDateOrderBySalesQuantityDesc(param.salesDate())
                .stream()
                .map(FindProductSalesResult::of)
                .toList();
    }
}
