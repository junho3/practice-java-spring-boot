package com.example.demo.core.product.service;

import com.example.demo.TestDataInsertSupport;
import com.example.demo.annotation.IntegrationTest;
import com.example.demo.common.enums.product.ProductStatus;
import com.example.demo.common.exceptions.BusinessErrorCode;
import com.example.demo.common.exceptions.BusinessException;
import com.example.demo.core.product.domain.Product;
import com.example.demo.core.stock.domain.Stock;
import com.example.demo.core.product.result.FindProductResult;
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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        @Nested
        @DisplayName("상품 데이터가 존재하지 않는다면")
        class Context_notFoundData {

            @Test
            @DisplayName("BusinessException을 던진다.")
            void it() {
                BusinessException exception = assertThrows(BusinessException.class, () ->
                    soldOutProductService.soldOut(PRODUCT_CODE)
                );

                assertEquals(BusinessErrorCode.NOT_FOUND_PRODUCT, exception.getBusinessErrorCode());
            }
        }

        @Nested
        @DisplayName("상품 데이터가 존재한다면")
        class Context_foundData {

            @Nested
            @DisplayName("상품 상태가 SELLING 이라면")
            class Context_selling {

                String productCode = "A202308041152";

                @BeforeEach
                void setUp () {
                    final Stock stock = new Stock(productCode, 10_000, 0);
                    save(stock);
                    save(new Product(productCode, PRODUCT_NAME, ProductStatus.SELLING, 100, stock));
                }

                @Test
                @DisplayName("SOLD_OUT 상태로 변경한다.")
                void it () {
                    FindProductResult result = soldOutProductService.soldOut(productCode);

                    Product product = productRepository.findByProductCode(productCode).get();

                    assertInstanceOf(FindProductResult.class, result);
                    assertEquals(SOLD_OUT, product.getProductStatus());
                }
            }

            @Nested
            @DisplayName("상품 상태가 END 라면")
            class Context_end {

                String productCode = "A202308041153";

                @BeforeEach
                void setUp () {
                    final Stock stock = new Stock(productCode, 10_000, 0);
                    save(stock);
                    save(new Product(productCode, PRODUCT_NAME, ProductStatus.END, 100, stock));
                }

                @Test
                @DisplayName("BusinessException을 던진다.")
                void it() {
                    BusinessException exception = assertThrows(BusinessException.class, () ->
                        soldOutProductService.soldOut(productCode)
                    );

                    assertEquals(BusinessErrorCode.NOT_POSSIBLE_CHANGE_END_TO_SOLD_OUT, exception.getBusinessErrorCode());
                }
            }
        }
    }
}
