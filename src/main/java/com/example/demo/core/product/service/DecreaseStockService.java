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
        // 여유 재고 >> 0개 미만 떨어질 가능성이 있음.. 5개를 여유 재고로 두고, 5개가 되었을 때 상품 상태를 솔드 아웃으로 변경함
        // GC를 위해 프리미티브 타입으로 변수 선언

        // 재고 수량은 남아 있어도, 솔드아웃 상태로 변경하는 것
        // 전시 on / off
        // 카테고리

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
