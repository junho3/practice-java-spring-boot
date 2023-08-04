package com.example.demo.core.product.service;

import com.example.demo.TestDataInsertSupport;
import com.example.demo.annotation.IntegrationTest;
import com.example.demo.common.exceptions.BusinessErrorCode;
import com.example.demo.common.exceptions.BusinessException;
import com.example.demo.core.product.domain.QStock;
import com.example.demo.core.product.domain.Stock;
import com.example.demo.core.product.param.IncreaseStockParam;
import com.example.demo.core.product.result.FindStockResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@IntegrationTest
@DisplayName("IncreaseStockService")
class IncreaseStockServiceTest extends TestDataInsertSupport {

    @Autowired
    IncreaseStockService increaseStockService;

    @Nested
    @DisplayName("increase 메소드는")
    class Describe_increase {

        @Nested
        @DisplayName("1번 재고를 10개 증가 시켰을 때")
        class Context_oneTime {

            @Nested
            @DisplayName("재고 정보가 존재 했을 때")
            class Context_existStock {

                final IncreaseStockParam param = new IncreaseStockParam(
                    Set.of(new IncreaseStockParam.Stock("A202307300140", 10))
                );

                @BeforeEach
                void setUp() {
                    save(new Stock("A202307300140", 0, 0));
                }

                @Test
                @DisplayName("재고를 10개로 증가시키고, 재고정보를 리턴한다.")
                void it() {
                    List<FindStockResult> stocks = increaseStockService.increase(param);

                    Stock result = jpaQueryFactory.selectFrom(QStock.stock)
                        .where(QStock.stock.productCode.eq("A202307300140"))
                        .fetchFirst();

                    assertEquals(10, result.getQuantity());
                    assertEquals(1, stocks.size());
                }
            }

            @Nested
            @DisplayName("재고 정보가 존재하지 않을 때")
            class Context_notExistStock {

                final IncreaseStockParam param = new IncreaseStockParam(
                    Set.of(new IncreaseStockParam.Stock("A202307300141", 10))
                );

                @Test
                @DisplayName("BusinessException을 던진다.")
                void it() {
                    BusinessException exception = assertThrows(BusinessException.class, () -> {
                        increaseStockService.increase(param);
                    });

                    assertEquals(BusinessErrorCode.NOT_FOUND_STOCK, exception.getBusinessErrorCode());
                }
            }
        }

        @Nested
        @DisplayName("동시에 10번의 재고를 증가시켰을 때")
        class Context_tenTimesAsTheSameTime {

            int threadCount = 10;

            final IncreaseStockParam param = new IncreaseStockParam(
                Set.of(
                    new IncreaseStockParam.Stock("A202307300142", 5),
                    new IncreaseStockParam.Stock("A202307300143", 5)
                )
            );

            @BeforeEach
            void setUp() {
                final List<Stock> stocks = List.of(
                    new Stock("A202307300142", 0, 0),
                    new Stock("A202307300143", 0, 0)
                );

                saveAll(stocks);
            }

            @Test
            @DisplayName("정상적으로 50개씩 재고를 증가시킨다.")
            void it() throws InterruptedException {
                ExecutorService executorService = Executors.newFixedThreadPool(5);
                CountDownLatch latch = new CountDownLatch(threadCount);

                for (int i = 0; i < threadCount; i++) {
                    executorService.submit(() -> {
                            try {
                                increaseStockService.increase(param);
                            } finally {
                                latch.countDown();
                            }
                        }
                    );
                }

                latch.await();

                List<Stock> results = jpaQueryFactory.selectFrom(QStock.stock)
                    .where(QStock.stock.productCode.in(List.of("A202307300142", "A202307300143")))
                    .fetch();

                assertAll(
                    () -> assertEquals(50, results.get(0).getQuantity()),
                    () -> assertEquals(50, results.get(1).getQuantity())
                );
            }
        }
    }
}
