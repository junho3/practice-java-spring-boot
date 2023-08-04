package com.example.demo.core.product.service;

import com.example.demo.TestDataInsertSupport;
import com.example.demo.annotation.IntegrationTest;
import com.example.demo.common.enums.product.ProductStatus;
import com.example.demo.core.product.domain.Product;
import com.example.demo.core.product.domain.Stock;
import com.example.demo.infrastructure.persistence.product.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.demo.ProductFixtures.PRODUCT_CODE;
import static com.example.demo.ProductFixtures.PRODUCT_NAME;
import static com.example.demo.common.enums.product.ProductStatus.SOLD_OUT;
import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
@DisplayName("CreateProductService")
class SoldOutProductServiceTest extends TestDataInsertSupport {

    @Autowired
    SoldOutProductService soldOutProductService;

    @Autowired
    ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Nested
    @DisplayName("soldOut 메소드는")
    class Describe_soldOut {

        String productCode = PRODUCT_CODE;

        @Nested
        @DisplayName("상품 데이터가 존재한다면")
        class Context_notFoundData {

            @BeforeEach
            void setUp() {
                final Stock stock = new Stock(productCode, 10_000, 0);
                save(stock);
                save(new Product(productCode, PRODUCT_NAME, ProductStatus.SELLING, 100, stock));
            }

            @Test
            @DisplayName("SOLD_OUT 상태로 변경한다.")
            void it() {
                soldOutProductService.soldOut(productCode);

                Product product = productRepository.findByProductCode(productCode).get();

                assertEquals(SOLD_OUT, product.getProductStatus());
            }
        }
    }
}
