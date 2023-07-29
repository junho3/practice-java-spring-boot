package com.example.demo.core.product.service;

import com.example.demo.annotation.IntegrationTest;
import com.example.demo.core.order.param.CreateOrderParam;
import com.example.demo.core.order.result.CreateOrderResult;
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

import java.util.Set;

import static com.example.demo.MemberFixtures.MEMBER_NO;
import static com.example.demo.OrderFixtures.ORDER_NO;
import static com.example.demo.ProductFixtures.PRODUCT_CODE;
import static com.example.demo.ProductFixtures.PRODUCT_NAME;
import static org.junit.jupiter.api.Assertions.*;

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

        final CreateProductParam param = new CreateProductParam(
            productCode,
            productName,
            1_000,
            1_000
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
