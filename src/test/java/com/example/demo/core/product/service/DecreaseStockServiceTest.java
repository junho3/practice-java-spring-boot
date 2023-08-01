package com.example.demo.core.product.service;

import com.example.demo.TestDataInsertSupport;
import com.example.demo.annotation.IntegrationTest;
import com.example.demo.common.enums.product.ProductStatus;
import com.example.demo.common.exceptions.BusinessErrorCode;
import com.example.demo.common.exceptions.BusinessException;
import com.example.demo.core.product.domain.Product;
import com.example.demo.core.product.domain.QStock;
import com.example.demo.core.product.domain.Stock;
import com.example.demo.core.product.param.DecreaseStockParam;
import com.example.demo.core.product.param.SearchProductParam;
import com.example.demo.core.product.result.FindProductResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demo.ProductFixtures.PRODUCT_NAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
@DisplayName("DecreaseStockService")
class DecreaseStockServiceTest extends TestDataInsertSupport {

    @Autowired
    DecreaseStockService decreaseStockService;

    @Nested
    @DisplayName("decrease 메소드는")
    class Describe_decrease {

        @BeforeEach
        void setUp() {
            final List<Stock> stocks = List.of(
                new Stock("A202307300134", 10),
                new Stock("A202307300135", 10),
                new Stock("A202307300136", 100),
                new Stock("A202307300137", 100),
                new Stock("A202307300138", 100),
                new Stock("A202307300139", 0)
            );

            saveAll(stocks);
        }

        @Nested
        @DisplayName("한명의 고객이 재고를 차감했을 때")
        class Context_oneCustomer {

            @Nested
            @DisplayName("재고가 충분하지 않은 경우")
            class Context_notEnoughQuantity {
                final DecreaseStockParam param = new DecreaseStockParam(
                    Set.of(
                        new DecreaseStockParam.Stock("A202307300134", 5),
                        new DecreaseStockParam.Stock("A202307300135", 5),
                        new DecreaseStockParam.Stock("A202307300139", 5)
                    )
                );

                @Test
                @DisplayName("BusinessException을 던지고, 차감한 재고를 롤백한다.")
                void it() {
                    BusinessException exception = assertThrows(BusinessException.class, () -> {
                        decreaseStockService.decrease(param);
                    });

                    List<Stock> results = jpaQueryFactory.selectFrom(QStock.stock).fetch();

                    assertAll(
                        () -> assertEquals(BusinessErrorCode.INVALID_STOCK_QUANTITY, exception.getBusinessErrorCode()),
                        () -> assertEquals(5, results.get(0).getQuantity()),
                        () -> assertEquals(5, results.get(1).getQuantity()),
                        () -> assertEquals(0, results.get(5).getQuantity())
                    );
                }
            }

            @Nested
            @DisplayName("재고가 충분할 경우")
            class Context_enoughQuantity {

                final DecreaseStockParam param = new DecreaseStockParam(
                    Set.of(
                        new DecreaseStockParam.Stock("A202307300134", 5),
                        new DecreaseStockParam.Stock("A202307300135", 5)
                    )
                );

                @Test
                @DisplayName("정상적으로 재고를 차감한다.")
                void it() {
                    decreaseStockService.decrease(param);

                    List<Stock> results = jpaQueryFactory.selectFrom(QStock.stock).fetch();
                    assertAll(
                        () -> assertEquals(5, results.get(0).getQuantity()),
                        () -> assertEquals(5, results.get(1).getQuantity())
                    );
                }
            }
        }
    }
}
