package com.example.demo.core.product.service;

import com.example.demo.core.product.domain.Stock;
import com.example.demo.core.product.param.DecreaseStockParam;
import com.example.demo.infrastructure.persistence.product.ProductRepository;
import com.example.demo.infrastructure.persistence.product.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Transactional
@Service
public class DecreaseStockService {

    private final StockRepository stockRepository;

    private final ProductRepository productRepository;

    public DecreaseStockService(StockRepository stockRepository, ProductRepository productRepository) {
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
    }

    public void decrease(DecreaseStockParam param) {
        // TODO 판매중인 상품인지 유효성 검사 필요

        param.getStocks()
            .stream()
            .sorted(Comparator.comparing(DecreaseStockParam.Stock::getProductCode))
            .collect(Collectors.toCollection(LinkedHashSet::new))
            .forEach(item -> {
                    Stock decreasedStock = stockRepository.findByProductCodeForUpdate(item.getProductCode())
                        .orElseThrow()
                        .decrease(item.getQuantity());

                    if (decreasedStock.isEmptyQuantity()) {
                        setSoldOut(item.getProductCode());
                    }
                }
            );
    }

    private void setSoldOut(String productCode) {
        productRepository.findByProductCode(productCode)
            .orElseThrow()
            .setSoldOut();
    }
}
