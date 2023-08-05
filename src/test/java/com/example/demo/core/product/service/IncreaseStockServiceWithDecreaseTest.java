package com.example.demo.core.product.service;

import com.example.demo.TestDataInsertSupport;
import com.example.demo.annotation.IntegrationTest;
import com.example.demo.core.stock.domain.QStock;
import com.example.demo.core.stock.domain.Stock;
import com.example.demo.core.stock.param.DecreaseStockParam;
import com.example.demo.core.stock.param.IncreaseStockParam;
import com.example.demo.core.stock.service.DecreaseStockService;
import com.example.demo.core.stock.service.IncreaseStockService;
import com.example.demo.infrastructure.persistence.product.ProductRepository;
import com.example.demo.infrastructure.persistence.stock.StockRepository;
import org.junit.jupiter.api.AfterEach;
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

@IntegrationTest
@DisplayName("IncreaseStockServiceWithDecrease")
class IncreaseStockServiceWithDecreaseTest extends TestDataInsertSupport {

    @Autowired
    IncreaseStockService increaseStockService;

    @Autowired
    DecreaseStockService decreaseStockService;

    @Autowired
    StockRepository stockRepository;

    @Autowired
    ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
        stockRepository.deleteAll();
    }

    @Nested
    @DisplayName("increase 메소드는")
    class Describe_increase {

        @Nested
        @DisplayName("동시에 10번의 재고 증가와 차감 시켰을 때")
        class Context_tenTimesAsTheSameTime {

            int threadCount = 10;

            final IncreaseStockParam increaseStockParam = new IncreaseStockParam(
                Set.of(
                    new IncreaseStockParam.Stock("A202307300150", 5),
                    new IncreaseStockParam.Stock("A202307300151", 5)
                )
            );

            final DecreaseStockParam decreaseStockParam = new DecreaseStockParam(
                Set.of(
                    new DecreaseStockParam.Stock("A202307300150", 1),
                    new DecreaseStockParam.Stock("A202307300151", 1)
                )
            );

            @BeforeEach
            void setUp() {
                final List<Stock> stocks = List.of(
                    new Stock("A202307300150", 0, 0),
                    new Stock("A202307300151", 0, 0)
                );

                saveAll(stocks);
            }

            @Test
            @DisplayName("재고를 50개 증가시키고, 10개를 차감시켜서 재고는 40개가 된다.")
            void it() throws InterruptedException {
                ExecutorService executorService = Executors.newFixedThreadPool(5);
                CountDownLatch latch = new CountDownLatch(threadCount);

                for (int i = 0; i < threadCount; i++) {
                    executorService.submit(() -> {
                            try {
                                increaseStockService.increase(increaseStockParam);
                                decreaseStockService.decrease(decreaseStockParam);
                            } finally {
                                latch.countDown();
                            }
                        }
                    );
                }

                latch.await();

                List<Stock> results = jpaQueryFactory.selectFrom(QStock.stock)
                    .where(QStock.stock.productCode.in(List.of("A202307300150", "A202307300151")))
                    .fetch();

                assertAll(
                    () -> assertEquals(40, results.get(0).getQuantity()),
                    () -> assertEquals(40, results.get(1).getQuantity())
                );
            }
        }
    }
}
