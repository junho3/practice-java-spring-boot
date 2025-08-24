package com.example.demo.core.stock.service;

import com.example.demo.core.product.service.SoldOutProductService;
import com.example.demo.core.stock.domain.Stock;
import com.example.demo.core.stock.param.DecreaseStockParam;
import com.example.demo.infrastructure.persistence.stock.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DecreaseStockService {

    private final StockRepository stockRepository;
    private final SoldOutProductService soldOutProductService;

    public void decrease(final DecreaseStockParam param) {
        param.stocks()
            .stream()
            .sorted(Comparator.comparing(DecreaseStockParam.Stock::productCode))
            .collect(Collectors.toCollection(LinkedHashSet::new))
            .forEach(item -> {
                    Stock decreasedStock = stockRepository.findByProductCodeForUpdate(item.productCode())
                        .orElseThrow()
                        .decrease(item.quantity());

                    log.info("[Decrease Stock] stockId: {} quantity: {}", decreasedStock.getStockId(), decreasedStock.getQuantity());

                    if (decreasedStock.isLimitQuantity()) {
                        soldOutProductService.soldOut(item.productCode());
                    }
                }
            );
    }
}
