package com.example.demo.core.product.service;

import com.example.demo.TestDataInsertSupport;
import com.example.demo.annotation.IntegrationTest;
import com.example.demo.common.exceptions.BusinessException;
import com.example.demo.core.product.domain.FoodProduct;
import com.example.demo.core.product.domain.Product;
import com.example.demo.core.product.result.FindProductResult;
import com.example.demo.core.stock.domain.Stock;
import com.example.demo.infrastructure.persistence.product.ProductRepository;
import com.example.demo.infrastructure.persistence.stock.StockRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.example.demo.ProductFixtures.PRODUCT_CODE;
import static com.example.demo.ProductFixtures.PRODUCT_NAME;
import static com.example.demo.common.enums.product.ProductStatus.SELLING;
import static com.example.demo.common.enums.product.ProductStatus.SOLD_OUT;
import static com.example.demo.common.exceptions.BusinessErrorCode.NOT_FOUND_PRODUCT;
import static com.example.demo.common.exceptions.BusinessErrorCode.NOT_POSSIBLE_CHANGE_SELLING_AS_STOCK_QUANTITY_EMPTY;
import static com.example.demo.common.exceptions.BusinessErrorCode.NOT_POSSIBLE_CHANGE_SELLING_AS_STOCK_QUANTITY_LESS_THAN_MIN_LIMIT_STOCK_QUANTITY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("SellProductService")
@IntegrationTest
@RequiredArgsConstructor
class SellProductServiceTest extends TestDataInsertSupport {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final SellProductService sellProductService;

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
                assertThatThrownBy(() -> sellProductService.sell(PRODUCT_CODE))
                    .isExactlyInstanceOf(BusinessException.class)
                    .extracting("businessErrorCode").isEqualTo(NOT_FOUND_PRODUCT);
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
                    save(new FoodProduct(productCode, PRODUCT_NAME, SOLD_OUT, 100, stock, LocalDate.now()));
                }

                @Test
                @DisplayName("BusinessException을 던진다")
                void it () {
                    assertThatThrownBy(() -> sellProductService.sell(productCode))
                        .isExactlyInstanceOf(BusinessException.class)
                        .extracting("businessErrorCode")
                        .isEqualTo(NOT_POSSIBLE_CHANGE_SELLING_AS_STOCK_QUANTITY_EMPTY);
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
                    save(new FoodProduct(productCode, PRODUCT_NAME, SOLD_OUT, 100, stock, LocalDate.now()));
                }

                @Test
                @DisplayName("BusinessException을 던진다.")
                void it() {
                    assertThatThrownBy(() -> sellProductService.sell(productCode))
                        .isExactlyInstanceOf(BusinessException.class)
                        .extracting("businessErrorCode")
                        .isEqualTo(NOT_POSSIBLE_CHANGE_SELLING_AS_STOCK_QUANTITY_LESS_THAN_MIN_LIMIT_STOCK_QUANTITY);
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
                    save(new FoodProduct(productCode, PRODUCT_NAME, SOLD_OUT, 100, stock, LocalDate.now()));
                }

                @Test
                @DisplayName("SELLING 상태로 변경한다.")
                void it() {
                    FindProductResult result = sellProductService.sell(productCode);

                    Product product = productRepository.findByProductCode(productCode).get();

                    assertThat(result).isExactlyInstanceOf(FindProductResult.class);
                    assertThat(product.getProductStatus()).isEqualTo(SELLING);
                }
            }
        }
    }
}
