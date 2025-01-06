package com.example.demo.core.product.service;

import com.example.demo.core.product.result.FindProductSalesRankingResult;
import com.example.demo.infrastructure.persistence.product.ProductSalesRankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindProductSalesRankingService {

    private final ProductSalesRankingRepository productSalesRankingRepository;

    public List<FindProductSalesRankingResult> top10(final LocalDate salesDate) {
        return productSalesRankingRepository.findTop10BySalesDateOrderBySalesQuantityDesc(salesDate)
            .stream()
            .map(FindProductSalesRankingResult::of)
            .toList();
    }
}
