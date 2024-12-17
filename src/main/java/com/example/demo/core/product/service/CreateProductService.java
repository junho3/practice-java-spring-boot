package com.example.demo.core.product.service;

import com.example.demo.common.exceptions.BusinessErrorCode;
import com.example.demo.common.exceptions.BusinessException;
import com.example.demo.core.product.domain.Product;
import com.example.demo.core.product.param.CreateProductParam;
import com.example.demo.core.stock.domain.Stock;
import com.example.demo.infrastructure.kafka.product.CreateProductPayload;
import com.example.demo.infrastructure.kafka.product.ProductKafkaPublisher;
import com.example.demo.infrastructure.persistence.product.ProductRepository;
import com.example.demo.infrastructure.persistence.stock.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateProductService {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final ProductKafkaPublisher productKafkaPublisher;

    public void create(final CreateProductParam param) {
        validateDuplicatedProduct(param.getProductCode());

        final Stock stock = stockRepository.save(param.toStockEntity());
        final Product product = productRepository.save(param.toProductEntity(stock));

        productKafkaPublisher.create(CreateProductPayload.builder()
            .productCode(product.getProductCode())
            .productName(product.getProductName())
            .productAmount(product.getProductAmount())
            .build());
    }

    private void validateDuplicatedProduct(String productCode) {
        Product product = productRepository.findByProductCode(productCode)
            .orElse(null);

        if (product != null) {
            throw new BusinessException(BusinessErrorCode.DUPLICATED_PRODUCT_CODE);
        }
    }
}
