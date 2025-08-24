package com.example.demo.core.stock.service;

import com.example.demo.TestDataInsertSupport;
import com.example.demo.TestFixtures;
import com.example.demo.annotation.IntegrationTest;
import com.example.demo.core.product.result.FindProductResult;
import com.example.demo.core.product.service.SoldOutProductService;
import com.example.demo.core.stock.domain.InvalidStockQuantityException;
import com.example.demo.core.stock.domain.QStock;
import com.example.demo.core.stock.domain.Stock;
import com.example.demo.core.stock.param.DecreaseStockParam;
import com.example.demo.infrastructure.persistence.stock.StockRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.demo.common.exceptions.BusinessErrorCode.INVALID_STOCK_QUANTITY;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@IntegrationTest
@RequiredArgsConstructor
@DisplayName("DecreaseStockPessimisticLockService")
class DecreaseStockPessimisticLockServiceTest extends TestDataInsertSupport {

    private final StockRepository stockRepository;
    private final DecreaseStockPessimisticLockService sut;

    @MockitoBean
    SoldOutProductService soldOutProductService;

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
                        TestFixtures.get().giveMeBuilder(FindProductResult.class)
                            .sample()
                    );
                }

                @Test
                @DisplayName("정상적으로 재고를 차감하고, 재고가 모두 소진된 상품은 SoldOut()을 호출한다.")
                void it() {
                    sut.decrease(param);

                    List<Stock> actual = jpaQueryFactory.selectFrom(QStock.stock)
                        .where(QStock.stock.productCode.in(List.of("A202307300130", "A202307300131")))
                        .fetch();

                    assertSoftly(it -> {
                        it.assertThat(actual.getFirst().getQuantity()).isEqualTo(0);
                        it.assertThat(actual.get(1).getQuantity()).isEqualTo(5);
                    });

                    verify(soldOutProductService).soldOut("A202307300130");
                    verify(soldOutProductService, never()).soldOut("A202307300131");
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
                @DisplayName("InvalidStockQuantityException을 던지고, 차감한 재고를 롤백한다.")
                void it() {
                    assertThatThrownBy(() -> sut.decrease(param))
                        .isExactlyInstanceOf(InvalidStockQuantityException.class)
                        .extracting("businessErrorCode")
                        .isEqualTo(INVALID_STOCK_QUANTITY);

                    List<Stock> actual = jpaQueryFactory.selectFrom(QStock.stock)
                        .where(QStock.stock.productCode.in(List.of("A202307300132", "A202307300133")))
                        .fetch();

                    assertSoftly(it -> {
                        it.assertThat(actual.getFirst().getQuantity()).isEqualTo(10);
                        it.assertThat(actual.get(1).getQuantity()).isEqualTo(0);
                    });
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
                    assertThatThrownBy(() -> sut.decrease(param))
                        .isExactlyInstanceOf(InvalidStockQuantityException.class)
                        .extracting("businessErrorCode")
                        .isEqualTo(INVALID_STOCK_QUANTITY);

                    List<Stock> actual = jpaQueryFactory.selectFrom(QStock.stock)
                        .where(QStock.stock.productCode.in(List.of("A202307300140", "A202307300141")))
                        .fetch();

                    assertSoftly(it -> {
                        it.assertThat(actual.getFirst().getQuantity()).isEqualTo(10);
                        it.assertThat(actual.get(1).getQuantity()).isEqualTo(10);
                    });
                }
            }
        }

        @Nested
        @DisplayName("동시에 총 10번의 재고를 차감했을 때")
        class Context_tenTimesAsTheSameTime {

            int threadCount = 5; // 스레드 개수
            int executeCount = 10; // 수행 회수

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
                    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
                    CountDownLatch startLatch = new CountDownLatch(1);
                    CountDownLatch endLatch = new CountDownLatch(executeCount);
                    AtomicInteger successCount = new AtomicInteger();
                    AtomicInteger failureCount = new AtomicInteger();

                    for (int i = 0; i < executeCount; i++) {
                        executorService.submit(() -> {
                                try {
                                    startLatch.await();
                                    sut.decrease(param);
                                    successCount.incrementAndGet();
                                } catch (Exception e) {
                                    failureCount.incrementAndGet();
                                } finally {
                                    endLatch.countDown();
                                }
                            }
                        );
                    }

                    startLatch.countDown();
                    endLatch.await();
                    executorService.shutdown();

                    List<Stock> actual = jpaQueryFactory.selectFrom(QStock.stock)
                        .where(QStock.stock.productCode.in(List.of("A202307300134", "A202307300135")))
                        .fetch();

                    assertSoftly(it -> {
                        it.assertThat(successCount.get()).isEqualTo(10);
                        it.assertThat(failureCount.get()).isEqualTo(0);
                        it.assertThat(actual.getFirst().getQuantity()).isEqualTo(50);
                        it.assertThat(actual.get(1).getQuantity()).isEqualTo(50);
                    });
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
                    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
                    CountDownLatch startLatch = new CountDownLatch(1);
                    CountDownLatch endLatch = new CountDownLatch(executeCount);
                    AtomicInteger successCount = new AtomicInteger();
                    AtomicInteger failureCount = new AtomicInteger();

                    for (int i = 0; i < executeCount; i++) {
                        executorService.submit(() -> {
                                try {
                                    startLatch.await();
                                    sut.decrease(param);
                                    successCount.incrementAndGet();
                                } catch (Exception e) {
                                    failureCount.incrementAndGet();
                                } finally {
                                    endLatch.countDown();
                                }
                            }
                        );
                    }

                    startLatch.countDown();
                    endLatch.await();
                    executorService.shutdown();

                    List<Stock> actual = jpaQueryFactory.selectFrom(QStock.stock)
                        .where(QStock.stock.productCode.in(List.of("A202307300136", "A202307300137")))
                        .fetch();

                    assertSoftly(it -> {
                        it.assertThat(successCount.get()).isEqualTo(9);
                        it.assertThat(failureCount.get()).isEqualTo(1);
                        it.assertThat(actual.getFirst().getQuantity()).isEqualTo(4);
                        it.assertThat(actual.get(1).getQuantity()).isEqualTo(4);
                    });
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
                        TestFixtures.get().giveMeBuilder(FindProductResult.class)
                            .sample()
                    );
                }

                @Test
                @DisplayName("정상적으로 50개씩 재고를 차감하고, 재고가 모두 소진된 상품은 SoldOut()을 호출한다.")
                void it() throws InterruptedException {
                    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
                    CountDownLatch startLatch = new CountDownLatch(1);
                    CountDownLatch endLatch = new CountDownLatch(executeCount);
                    AtomicInteger successCount = new AtomicInteger();
                    AtomicInteger failureCount = new AtomicInteger();

                    for (int i = 0; i < executeCount; i++) {
                        executorService.submit(() -> {
                                try {
                                    startLatch.await();
                                    sut.decrease(param);
                                    successCount.incrementAndGet();
                                } catch (Exception e) {
                                    failureCount.incrementAndGet();
                                } finally {
                                    endLatch.countDown();
                                }
                            }
                        );
                    }

                    startLatch.countDown();
                    endLatch.await();
                    executorService.shutdown();

                    List<Stock> actual = jpaQueryFactory.selectFrom(QStock.stock)
                        .where(QStock.stock.productCode.in(List.of("A202307300138", "A202307300139")))
                        .fetch();

                    assertSoftly(it -> {
                        it.assertThat(successCount.get()).isEqualTo(10);
                        it.assertThat(failureCount.get()).isEqualTo(0);
                        it.assertThat(actual.getFirst().getQuantity()).isEqualTo(0);
                        it.assertThat(actual.get(1).getQuantity()).isEqualTo(1);
                    });

                    verify(soldOutProductService).soldOut("A202307300138");
                    verify(soldOutProductService, never()).soldOut("A202307300139");
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
                        TestFixtures.get().giveMeBuilder(FindProductResult.class)
                            .sample()
                    );
                }

                @Test
                @DisplayName("정상적으로 50개씩 재고를 차감하고, 최소 제한 재고수량인 상품은 SoldOut()을 호출한다.")
                void it() throws InterruptedException {
                    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
                    CountDownLatch startLatch = new CountDownLatch(1);
                    CountDownLatch endLatch = new CountDownLatch(executeCount);
                    AtomicInteger successCount = new AtomicInteger();
                    AtomicInteger failureCount = new AtomicInteger();

                    for (int i = 0; i < executeCount; i++) {
                        executorService.submit(() -> {
                                try {
                                    startLatch.await();
                                    sut.decrease(param);
                                    successCount.incrementAndGet();
                                } catch (Exception e) {
                                    failureCount.incrementAndGet();
                                } finally {
                                    endLatch.countDown();
                                }
                            }
                        );
                    }

                    startLatch.countDown();
                    endLatch.await();
                    executorService.shutdown();

                    List<Stock> actual = jpaQueryFactory.selectFrom(QStock.stock)
                        .where(QStock.stock.productCode.in(List.of("A202307300142", "A202307300143")))
                        .fetch();

                    assertSoftly(it -> {
                        it.assertThat(successCount.get()).isEqualTo(10);
                        it.assertThat(failureCount.get()).isEqualTo(0);
                        it.assertThat(actual.getFirst().getQuantity()).isEqualTo(50);
                        it.assertThat(actual.get(1).getQuantity()).isEqualTo(50);
                    });

                    verify(soldOutProductService).soldOut("A202307300142");
                    verify(soldOutProductService, never()).soldOut("A202307300143");
                }
            }
        }
    }
}
