package com.example.demo.core.product.service;

import com.example.demo.annotation.IntegrationTest;
import com.example.demo.core.product.domain.Product;
import com.example.demo.core.product.domain.Stock;
import com.example.demo.core.product.param.CreateProductParam;
import com.example.demo.infrastructure.persistence.product.ProductRepository;
import com.example.demo.infrastructure.persistence.product.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static com.example.demo.ProductFixtures.PRODUCT_CODE;
import static com.example.demo.ProductFixtures.PRODUCT_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@IntegrationTest
class CreateProductServiceTest {

    @Autowired
    private CreateProductService createProductService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
        stockRepository.deleteAll();
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {

        private final String productCode = PRODUCT_CODE;
        private final String productName = PRODUCT_NAME;
        private final long quantity = 1_000;

        final CreateProductParam param = new CreateProductParam(
            productCode,
            productName,
            1_000,
            quantity
        );

        @AfterEach
        void tearDown() {
            productRepository.deleteAll();
            stockRepository.deleteAll();
        }

        @Nested
        @DisplayName("중복된 상품번호가 존재하지 않는다면")
        class Context_createNewProduct {

            @Test
            @DisplayName("Product와 Stock을 생성한다.")
            void it() {
                createProductService.create(param);

                Product product = productRepository.findByProductCode(productCode)
                    .orElseThrow();
                assertEquals(productName, product.getProductName());

                Stock stock = stockRepository.findByProductCode(productCode)
                        .orElseThrow();
                assertEquals(quantity, stock.getQuantity());
            }
        }

        @Nested
        @DisplayName("중복된 상품번호가 존재한다면")
        class Context_duplicateProduct {

            @BeforeEach
            void setUp() {
                createProductService.create(param);
            }

            @Test
            @DisplayName("DataIntegrityViolationException을 던진다.")
            void it() {
                assertThrows(DataIntegrityViolationException.class, () -> {
                    createProductService.create(param);
                });
            }
        }
    }
}
