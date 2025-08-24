package com.example.demo.core.stock.service;

import com.example.demo.core.stock.domain.InvalidStockQuantityException;
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
public class DecreaseStockNativeQueryService implements DecreaseStockService {

    private final StockRepository stockRepository;

    @Override
    public void decrease(final DecreaseStockParam param) {
        param.stocks()
            .stream()
            .sorted(Comparator.comparing(DecreaseStockParam.Stock::productCode))
            .collect(Collectors.toCollection(LinkedHashSet::new))
            .forEach(it -> {
                int success = stockRepository.decreaseQuantity(it.productCode(), it.quantity());

                if (success == 0) {
                    throw new InvalidStockQuantityException();
                }

                // TODO SOLD OUT 처리 필요
            });
    }
}
