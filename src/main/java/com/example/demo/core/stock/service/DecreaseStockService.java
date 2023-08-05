package com.example.demo.core.stock.service;

import com.example.demo.core.stock.domain.Stock;
import com.example.demo.core.product.service.SoldOutProductService;
import com.example.demo.core.stock.param.DecreaseStockParam;
import com.example.demo.infrastructure.persistence.stock.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Transactional
@Service
public class DecreaseStockService {

    private final StockRepository stockRepository;

    private final SoldOutProductService soldOutProductService;

    public DecreaseStockService(StockRepository stockRepository, SoldOutProductService soldOutProductService) {
        this.stockRepository = stockRepository;
        this.soldOutProductService = soldOutProductService;
    }

    public void decrease(DecreaseStockParam param) {
        param.getStocks()
            .stream()
            .sorted(Comparator.comparing(DecreaseStockParam.Stock::getProductCode))
            .collect(Collectors.toCollection(LinkedHashSet::new))
            .forEach(item -> {
                    Stock decreasedStock = stockRepository.findByProductCodeForUpdate(item.getProductCode())
                        .orElseThrow()
                        .decrease(item.getQuantity());

                    if (decreasedStock.isLimitQuantity()) {
                        soldOutProductService.soldOut(item.getProductCode());
                    }
                }
            );
    }
}
