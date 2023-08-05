package com.example.demo.core.product.service;

import com.example.demo.TestDataInsertSupport;
import com.example.demo.annotation.IntegrationTest;
import com.example.demo.common.enums.product.ProductStatus;
import com.example.demo.common.exceptions.BusinessErrorCode;
import com.example.demo.common.exceptions.BusinessException;
import com.example.demo.core.product.domain.QStock;
import com.example.demo.core.stock.domain.Stock;
import com.example.demo.core.stock.param.DecreaseStockParam;
import com.example.demo.core.product.result.FindProductResult;
import com.example.demo.core.stock.service.DecreaseStockService;
import com.example.demo.infrastructure.persistence.product.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.demo.ProductFixtures.PRODUCT_NAME;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@IntegrationTest
@DisplayName("DecreaseStockService")
class DecreaseStockServiceTest extends TestDataInsertSupport {

    @Autowired
    StockRepository stockRepository;

    @MockBean
    SoldOutProductService soldOutProductService;

    @Autowired
    DecreaseStockService decreaseStockService;

    @AfterEach
    void tearDown() {
        stockRepository.deleteAll();
    }

    @Nested
    @DisplayName("decrease 메소드는")
    class Describe_decrease {

        @Nested
        @DisplayName("1번 재고를 차감했을 때")
        class Context_oneTime {

            @Nested
            @DisplayName("재고가 충분할 경우")
            class Context_enoughQuantity {

                final DecreaseStockParam param = new DecreaseStockParam(
                    Set.of(
                        new DecreaseStockParam.Stock("A202307300130", 5),
                        new DecreaseStockParam.Stock("A202307300131", 5)
                    )
                );

                @BeforeEach
                void setUp() {
                    final List<Stock> stocks = List.of(
                        new Stock("A202307300130", 5, 0),
                        new Stock("A202307300131", 10, 0)
                    );
                    saveAll(stocks);

                    when(soldOutProductService.soldOut("A202307300130")).thenReturn(
                        new FindProductResult(
                            "A202307300130",
                            PRODUCT_NAME,
                            ProductStatus.SOLD_OUT,
                            100,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                        )
                    );
                }

                @Test
                @DisplayName("정상적으로 재고를 차감하고, 재고가 모두 소진된 상품은 SoldOut()을 호출한다.")
                void it() {
                    decreaseStockService.decrease(param);

                    List<Stock> results = jpaQueryFactory.selectFrom(QStock.stock)
                        .where(QStock.stock.productCode.in(List.of("A202307300130", "A202307300131")))
                        .fetch();

                    assertAll(
                        () -> assertEquals(0, results.get(0).getQuantity()),
                        () -> assertEquals(5, results.get(1).getQuantity()),
                        () -> verify(soldOutProductService, times(1)).soldOut("A202307300130"),
                        () -> verify(soldOutProductService, times(0)).soldOut("A202307300131")
                    );
                }
            }

            @Nested
            @DisplayName("재고가 충분하지 않은 경우")
            class Context_notEnoughQuantity {
                final DecreaseStockParam param = new DecreaseStockParam(
                    Set.of(
                        new DecreaseStockParam.Stock("A202307300132", 5),
                        new DecreaseStockParam.Stock("A202307300133", 5)
                    )
                );

                @BeforeEach
                void setUp() {
                    final List<Stock> stocks = List.of(
                        new Stock("A202307300132", 10, 0),
                        new Stock("A202307300133", 0, 0)
                    );
                    saveAll(stocks);
                }

                @Test
                @DisplayName("BusinessException을 던지고, 차감한 재고를 롤백한다.")
                void it() {
                    BusinessException exception = assertThrows(BusinessException.class, () -> {
                        decreaseStockService.decrease(param);
                    });

                    List<Stock> results = jpaQueryFactory.selectFrom(QStock.stock)
                        .where(QStock.stock.productCode.in(List.of("A202307300132", "A202307300133")))
                        .fetch();

                    assertAll(
                        () -> assertEquals(BusinessErrorCode.INVALID_STOCK_QUANTITY, exception.getBusinessErrorCode()),
                        () -> assertEquals(10, results.get(0).getQuantity()),
                        () -> assertEquals(0, results.get(1).getQuantity())
                    );
                }
            }

            @Nested
            @DisplayName("최소 제한 재고량이 7인 경우")
            class Context_minLimitQuantity {

                final DecreaseStockParam param = new DecreaseStockParam(
                    Set.of(
                        new DecreaseStockParam.Stock("A202307300140", 5),
                        new DecreaseStockParam.Stock("A202307300141", 5)
                    )
                );

                @BeforeEach
                void setUp() {
                    final List<Stock> stocks = List.of(
                        new Stock("A202307300140", 10, 7),
                        new Stock("A202307300141", 10, 7)
                    );
                    saveAll(stocks);
                }

                @Test
                @DisplayName("BusinessException을 던지고, 차감한 재고를 롤백한다.")
                void it() {
                    BusinessException exception = assertThrows(BusinessException.class, () -> {
                        decreaseStockService.decrease(param);
                    });

                    List<Stock> results = jpaQueryFactory.selectFrom(QStock.stock)
                        .where(QStock.stock.productCode.in(List.of("A202307300140", "A202307300141")))
                        .fetch();

                    assertAll(
                        () -> assertEquals(BusinessErrorCode.INVALID_STOCK_QUANTITY, exception.getBusinessErrorCode()),
                        () -> assertEquals(10, results.get(0).getQuantity()),
                        () -> assertEquals(10, results.get(1).getQuantity())
                    );
                }
            }
        }

        @Nested
        @DisplayName("동시에 10번의 재고를 차감했을 때")
        class Context_tenTimesAsTheSameTime {

            int threadCount = 10;

            @Nested
            @DisplayName("재고가 충분할 경우")
            class Context_enoughQuantity {

                final DecreaseStockParam param = new DecreaseStockParam(
                    Set.of(
                        new DecreaseStockParam.Stock("A202307300134", 5),
                        new DecreaseStockParam.Stock("A202307300135", 5)
                    )
                );

                @BeforeEach
                void setUp() {
                    final List<Stock> stocks = List.of(
                        new Stock("A202307300134", 100, 0),
                        new Stock("A202307300135", 100, 0)
                    );
                    saveAll(stocks);
                }

                @Test
                @DisplayName("정상적으로 50개씩 재고를 차감한다.")
                void it() throws InterruptedException {
                    ExecutorService executorService = Executors.newFixedThreadPool(5);
                    CountDownLatch latch = new CountDownLatch(threadCount);

                    for (int i = 0; i < threadCount; i++) {
                        executorService.submit(() -> {
                                try {
                                    decreaseStockService.decrease(param);
                                } finally {
                                    latch.countDown();
                                }
                            }
                        );
                    }

                    latch.await();

                    List<Stock> results = jpaQueryFactory.selectFrom(QStock.stock)
                        .where(QStock.stock.productCode.in(List.of("A202307300134", "A202307300135")))
                        .fetch();

                    assertAll(
                        () -> assertEquals(50, results.get(0).getQuantity()),
                        () -> assertEquals(50, results.get(1).getQuantity())
                    );
                }
            }

            @Nested
            @DisplayName("재고가 충분하지 않은 경우")
            class Context_notEnoughQuantity {
                final DecreaseStockParam param = new DecreaseStockParam(
                    Set.of(
                        new DecreaseStockParam.Stock("A202307300136", 5),
                        new DecreaseStockParam.Stock("A202307300137", 5)
                    )
                );

                @BeforeEach
                void setUp() {
                    final List<Stock> stocks = List.of(
                        new Stock("A202307300136", 49, 0),
                        new Stock("A202307300137", 49, 0)
                    );
                    saveAll(stocks);
                }

                @Test
                @DisplayName("BusinessException을 던지고, 마지막 차감 실패한 재고를 롤백하여 4개가 남는다.")
                void it() throws InterruptedException {
                    ExecutorService executorService = Executors.newFixedThreadPool(5);
                    CountDownLatch latch = new CountDownLatch(threadCount);

                    for (int i = 0; i < threadCount; i++) {
                        executorService.submit(() -> {
                                try {
                                    decreaseStockService.decrease(param);
                                } finally {
                                    latch.countDown();
                                }
                            }
                        );
                    }
                    latch.await();

                    List<Stock> results = jpaQueryFactory.selectFrom(QStock.stock)
                        .where(QStock.stock.productCode.in(List.of("A202307300136", "A202307300137")))
                        .fetch();

                    assertAll(
                        () -> assertEquals(4, results.get(0).getQuantity()),
                        () -> assertEquals(4, results.get(1).getQuantity())
                    );
                }
            }

            @Nested
            @DisplayName("재고가 모두 소진되었을 경우")
            class Context_emptyQuantity {

                final DecreaseStockParam param = new DecreaseStockParam(
                    Set.of(
                        new DecreaseStockParam.Stock("A202307300138", 5),
                        new DecreaseStockParam.Stock("A202307300139", 5)
                    )
                );

                @BeforeEach
                void setUp() {
                    final List<Stock> stocks = List.of(
                        new Stock("A202307300138", 50, 0),
                        new Stock("A202307300139", 51, 0)
                    );
                    saveAll(stocks);

                    when(soldOutProductService.soldOut("A202307300138")).thenReturn(
                        new FindProductResult(
                            "A202307300138",
                            PRODUCT_NAME,
                            ProductStatus.SOLD_OUT,
                            100,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                        )
                    );
                }

                @Test
                @DisplayName("정상적으로 50개씩 재고를 차감하고, 재고가 모두 소진된 상품은 SoldOut()을 호출한다.")
                void it() throws InterruptedException {
                    ExecutorService executorService = Executors.newFixedThreadPool(5);
                    CountDownLatch latch = new CountDownLatch(threadCount);

                    for (int i = 0; i < threadCount; i++) {
                        executorService.submit(() -> {
                                try {
                                    decreaseStockService.decrease(param);
                                } finally {
                                    latch.countDown();
                                }
                            }
                        );
                    }

                    latch.await();

                    List<Stock> results = jpaQueryFactory.selectFrom(QStock.stock)
                        .where(QStock.stock.productCode.in(List.of("A202307300138", "A202307300139")))
                        .fetch();

                    assertAll(
                        () -> assertEquals(0, results.get(0).getQuantity()),
                        () -> assertEquals(1, results.get(1).getQuantity()),
                        () -> verify(soldOutProductService, times(1)).soldOut("A202307300138"),
                        () -> verify(soldOutProductService, times(0)).soldOut("A202307300139")
                    );
                }
            }

            @Nested
            @DisplayName("최소 제한 재고수량이 되었을 경우")
            class Context_minLimitQuantity {

                final DecreaseStockParam param = new DecreaseStockParam(
                    Set.of(
                        new DecreaseStockParam.Stock("A202307300142", 5),
                        new DecreaseStockParam.Stock("A202307300143", 5)
                    )
                );

                @BeforeEach
                void setUp() {
                    final List<Stock> stocks = List.of(
                        new Stock("A202307300142", 100, 50),
                        new Stock("A202307300143", 100, 49)
                    );
                    saveAll(stocks);

                    when(soldOutProductService.soldOut("A202307300142")).thenReturn(
                        new FindProductResult(
                            "A202307300142",
                            PRODUCT_NAME,
                            ProductStatus.SOLD_OUT,
                            100,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                        )
                    );
                }

                @Test
                @DisplayName("정상적으로 50개씩 재고를 차감하고, 최소 제한 재고수량인 상품은 SoldOut()을 호출한다.")
                void it() throws InterruptedException {
                    ExecutorService executorService = Executors.newFixedThreadPool(5);
                    CountDownLatch latch = new CountDownLatch(threadCount);

                    for (int i = 0; i < threadCount; i++) {
                        executorService.submit(() -> {
                                try {
                                    decreaseStockService.decrease(param);
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
                        () -> assertEquals(50, results.get(1).getQuantity()),
                        () -> verify(soldOutProductService, times(1)).soldOut("A202307300142"),
                        () -> verify(soldOutProductService, times(0)).soldOut("A202307300143")
                    );
                }
            }
        }
    }
}
