package com.example.demo.core.stock.service;

import com.example.demo.common.exceptions.BusinessErrorCode;
import com.example.demo.common.exceptions.BusinessException;
import com.example.demo.core.stock.domain.Stock;
import com.example.demo.core.stock.param.IncreaseStockParam;
import com.example.demo.core.stock.result.FindStockResult;
import com.example.demo.infrastructure.persistence.stock.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class IncreaseStockService {

    private final StockRepository stockRepository;

    public IncreaseStockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<FindStockResult> increase(IncreaseStockParam param) {
        return param.getStocks()
            .stream()
            .sorted(Comparator.comparing(IncreaseStockParam.Stock::getProductCode))
            .collect(Collectors.toCollection(LinkedHashSet::new))
            .stream()
            .map(item -> {
                    Stock increasedStock = stockRepository.findByProductCodeForUpdate(item.getProductCode())
                        .orElseThrow(() -> new BusinessException(BusinessErrorCode.NOT_FOUND_STOCK))
                        .increase(item.getQuantity());

                    return FindStockResult.from(increasedStock);
                }
            )
            .collect(Collectors.toList());
    }
}
