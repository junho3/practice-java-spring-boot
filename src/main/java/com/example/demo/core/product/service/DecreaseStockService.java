package com.example.demo.core.product.service;

import com.example.demo.core.product.domain.Stock;
import com.example.demo.core.product.param.DecreaseStockParam;
import com.example.demo.infrastructure.persistence.product.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
public class DecreaseStockService {

    private final StockRepository stockRepository;

    public DecreaseStockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void decrease(DecreaseStockParam param) {
        param.getStocks()
            .stream()
            .sorted(Comparator.comparing(DecreaseStockParam.Stock::getProductCode))
            .collect(Collectors.toCollection(LinkedHashSet::new))
            .forEach(item -> stockRepository.findByProductCodeForUpdate(item.getProductCode())
                .orElseThrow()
                .decrease(item.getQuantity())
            );
    }
}
