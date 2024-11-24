package com.example.demo.core.product.service;

import com.example.demo.TestDataInsertSupport;
import com.example.demo.annotation.IntegrationTest;
import com.example.demo.common.exceptions.BusinessErrorCode;
import com.example.demo.common.exceptions.BusinessException;
import com.example.demo.core.product.domain.Product;
import com.example.demo.core.product.result.FindProductResult;
import com.example.demo.core.stock.domain.Stock;
import com.example.demo.infrastructure.persistence.product.ProductRepository;
import com.example.demo.infrastructure.persistence.stock.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.demo.ProductFixtures.PRODUCT_CODE;
import static com.example.demo.ProductFixtures.PRODUCT_NAME;
import static com.example.demo.common.enums.product.ProductStatus.SELLING;
import static com.example.demo.common.enums.product.ProductStatus.SOLD_OUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

@IntegrationTest
@DisplayName("SellProductService")
class SellProductServiceTest extends TestDataInsertSupport {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    StockRepository stockRepository;

    @Autowired
    SellProductService sellProductService;

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
        stockRepository.deleteAll();
    }

    @Nested
    @DisplayName("sell 메소드는")
    class Describe_sell {

        @Nested
        @DisplayName("상품 데이터가 존재하지 않는다면")
        class Context_notFoundData {

            @Test
            @DisplayName("BusinessException을 던진다.")
            void it() {
                BusinessException exception = assertThrows(BusinessException.class, () ->
                    sellProductService.sell(PRODUCT_CODE)
                );

                assertEquals(BusinessErrorCode.NOT_FOUND_PRODUCT, exception.getBusinessErrorCode());
            }
        }

        @Nested
        @DisplayName("상품 데이터가 존재한다면")
        class Context_foundData {

            @Nested
            @DisplayName("재고 수량이 0개라면")
            class Context_emptyStockQuantity {

                String productCode = "A202308051248";

                @BeforeEach
                void setUp () {
                    final Stock stock = new Stock(productCode, 0, 0);
                    save(stock);
                    save(new Product(productCode, PRODUCT_NAME, SOLD_OUT, 100, stock));
                }

                @Test
                @DisplayName("BusinessException을 던진다")
                void it () {
                    BusinessException exception = assertThrows(BusinessException.class, () ->
                        sellProductService.sell(productCode)
                    );

                    assertEquals(
                        BusinessErrorCode.NOT_POSSIBLE_CHANGE_SELLING_AS_STOCK_QUANTITY_EMPTY,
                        exception.getBusinessErrorCode()
                    );
                }
            }

            @Nested
            @DisplayName("재고 수량이 최소 제한 수량보다 같거나 낮을 경우")
            class Context_lessThanMinLimitStockQuantity {

                String productCode = "A202308051250";

                @BeforeEach
                void setUp () {
                    final Stock stock = new Stock(productCode, 10, 10);
                    save(stock);
                    save(new Product(productCode, PRODUCT_NAME, SOLD_OUT, 100, stock));
                }

                @Test
                @DisplayName("BusinessException을 던진다.")
                void it() {
                    BusinessException exception = assertThrows(BusinessException.class, () ->
                        sellProductService.sell(productCode)
                    );

                    assertEquals(
                        BusinessErrorCode.NOT_POSSIBLE_CHANGE_SELLING_AS_STOCK_QUANTITY_LESS_THAN_MIN_LIMIT_STOCK_QUANTITY,
                        exception.getBusinessErrorCode()
                    );
                }
            }

            @Nested
            @DisplayName("재고가 충분할 경우")
            class Context_enoughStockQuantity {

                String productCode = "A202308051250";

                @BeforeEach
                void setUp () {
                    final Stock stock = new Stock(productCode, 100, 10);
                    save(stock);
                    save(new Product(productCode, PRODUCT_NAME, SOLD_OUT, 100, stock));
                }

                @Test
                @DisplayName("SELLING 상태로 변경한다.")
                void it() {
                    FindProductResult result = sellProductService.sell(productCode);

                    Product product = productRepository.findByProductCode(productCode).get();

                    assertInstanceOf(FindProductResult.class, result);
                    assertEquals(SELLING, product.getProductStatus());
                }
            }
        }
    }
}
