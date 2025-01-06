package com.example.demo.core.product.service;

import com.example.demo.core.product.result.FindProductSalesResult;
import com.example.demo.infrastructure.persistence.product.ProductSalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindProductSalesService {

    private final ProductSalesRepository productSalesRepository;

    public List<FindProductSalesResult> top10(final LocalDate salesDate) {
        return productSalesRepository.findTop10BySalesDateOrderBySalesQuantityDesc(salesDate)
            .stream()
            .map(FindProductSalesResult::of)
            .toList();
    }
}
