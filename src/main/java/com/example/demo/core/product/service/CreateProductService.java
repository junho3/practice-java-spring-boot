package com.example.demo.core.product.service;

import com.example.demo.common.exceptions.BusinessErrorCode;
import com.example.demo.common.exceptions.BusinessException;
import com.example.demo.core.product.domain.Product;
import com.example.demo.core.stock.domain.Stock;
import com.example.demo.core.product.param.CreateProductParam;
import com.example.demo.infrastructure.persistence.product.ProductRepository;
import com.example.demo.infrastructure.persistence.stock.StockRepository;
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
        validateDuplicatedProduct(param.getProductCode());

        Stock stock = stockRepository.save(param.toStockEntity());
        productRepository.save(param.toProductEntity(stock));
    }

    private void validateDuplicatedProduct(String productCode) {
        Product product = productRepository.findByProductCode(productCode)
            .orElse(null);

        if (product != null) {
            throw new BusinessException(BusinessErrorCode.DUPLICATED_PRODUCT_CODE);
        }
    }
}
