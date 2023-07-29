package com.example.demo.core.product.service;

import com.example.demo.core.product.domain.Product;
import com.example.demo.core.product.domain.Stock;
import com.example.demo.core.product.param.CreateProductParam;
import com.example.demo.infrastructure.persistence.product.ProductRepository;
import com.example.demo.infrastructure.persistence.product.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CreateProductService {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    public CreateProductService(ProductRepository productRepository, StockRepository stockRepository) {
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
    }

    public void create(CreateProductParam param) {
        Stock stock = stockRepository.save(param.toStockEntity());

        Product product = productRepository.save(param.toProductEntity(stock));
    }
}
