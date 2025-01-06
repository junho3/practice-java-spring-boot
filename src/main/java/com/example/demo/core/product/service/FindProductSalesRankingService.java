package com.example.demo.core.product.service;

import com.example.demo.infrastructure.persistence.product.ProductSalesRankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindProductSalesRankingService {

    private final ProductSalesRankingRepository productSalesRankingRepository;

    public void top10() {
        productSalesRankingRepository.findTop10BySalesDateOrderBySalesQuantityDesc(LocalDate.now());
    }
}
