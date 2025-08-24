package com.example.demo.core.stock.service;

import com.example.demo.TestDataInsertSupport;
import com.example.demo.annotation.IntegrationTest;
import com.example.demo.core.stock.domain.Stock;
import com.example.demo.core.stock.param.DecreaseStockParam;
import com.example.demo.infrastructure.persistence.stock.StockRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@IntegrationTest
@RequiredArgsConstructor
@DisplayName("DecreaseStockNativeQueryService")
class DecreaseStockNativeQueryServiceTest extends TestDataInsertSupport {

    private final StockRepository stockRepository;
    private final DecreaseStockNativeQueryService sut;

    private final String PRODUCT_CODE = "A202307300138";
    private final int threads = 5; // 스레드 수
    private final int executeCount = 10; // 테스트 수행 횟수

    @BeforeEach
    void setUp() {
        // 매 테스트마다 초기 재고 세팅
        stockRepository.deleteAll();
        save(new Stock(PRODUCT_CODE, 100, 0)); // 초기 재고 100
    }

    @Test
    @DisplayName("100개의 재고를 5개씩 동시에 10번 차감하여 남은 재고는 50개이다.")
    void test1() throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(threads);
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch endLatch = new CountDownLatch(executeCount);
        final AtomicInteger successCount = new AtomicInteger(0);
        final AtomicInteger failureCount = new AtomicInteger(0);

        final var param = new DecreaseStockParam(Set.of(new DecreaseStockParam.Stock(PRODUCT_CODE, 5L)));

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

        // 검증
        var actual = stockRepository.findByProductCode(PRODUCT_CODE).stream()
            .findFirst()
            .orElseThrow();

        assertSoftly(it -> {
            it.assertThat(successCount.get()).isEqualTo(10);
            it.assertThat(failureCount.get()).isEqualTo(0);
            it.assertThat(actual.getQuantity()).isEqualTo(50);
        });
    }

    @Test
    @DisplayName("100개의 재고를 11개씩 동시에 10번 차감했을 때 9번까지 99개를 차감하고, 10번째 차감은 실패하여 남은 재고는 1개이다.")
    void test2() throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(threads);
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch endLatch = new CountDownLatch(executeCount);
        final AtomicInteger successCount = new AtomicInteger(0);
        final AtomicInteger failureCount = new AtomicInteger(0);

        final var param = new DecreaseStockParam(Set.of(new DecreaseStockParam.Stock(PRODUCT_CODE, 11L)));

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

        // 검증
        var actual = stockRepository.findByProductCode(PRODUCT_CODE).stream()
            .findFirst()
            .orElseThrow();

        assertSoftly(it -> {
            it.assertThat(successCount.get()).isEqualTo(9);
            it.assertThat(failureCount.get()).isEqualTo(1);
            it.assertThat(actual.getQuantity()).isEqualTo(1);
        });
    }
}
