package com.example.demo.infrastructure.persistence.stock;

import com.example.demo.TestFixtures;
import com.example.demo.annotation.RepositoryTest;
import com.example.demo.core.stock.domain.Stock;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
@DisplayName("StockRepository")
class StockRepositoryTest {

    @Autowired
    private StockRepository sut;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        sut.deleteAll();

        entityManager.flush();
        entityManager.clear();

        var expected = IntStream.rangeClosed(1, 10_000)
            .mapToObj(String::valueOf)
            .collect(Collectors.toList())
            .iterator();

        var stocks = TestFixtures.get().giveMeBuilder(Stock.class)
            .setNull("stockId")
            .setLazy("productCode", expected::next)
            .sampleList(10_000);

        sut.saveAll(stocks);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("streamAll()은 데이터를 한 번에 한 건씩 가져온다.")
    void test1() {
        var runtime = Runtime.getRuntime();
        var memoryBefore = runtime.totalMemory() - runtime.freeMemory();

        long actual = 0;
        try (var stream = sut.streamAll()) {
            actual = stream.peek(it -> {
                // 중간마다 메모리 사용량 확인
                if (it.getStockId() % 1_000 == 0) {
                    var current = runtime.totalMemory() - runtime.freeMemory();
                    System.out.println("Processed: " + it.getStockId() + ", Memory used: " + (current - memoryBefore) / 1024 / 1024 + "MB");
                }
            }).count(); // 실제 데이터를 한 줄씩 읽음
        }

        var memoryAfter = runtime.totalMemory() - runtime.freeMemory();

        System.out.println("총 처리 건수: " + actual);
        System.out.println("메모리 사용 증가량: " + (memoryAfter - memoryBefore) / 1024 / 1024 + "MB");

        assertThat(actual).isEqualTo(10_000);
    }

    @Test
    @DisplayName("findAll()은 데이터를 한 번에 모두 가져온다.")
    void test2() {
        var runtime = Runtime.getRuntime();
        var memoryBefore = runtime.totalMemory() - runtime.freeMemory();

        var actual = sut.findAll();

        var memoryAfter = runtime.totalMemory() - runtime.freeMemory();

        System.out.println("findAll memory usage: " + (memoryAfter - memoryBefore) / 1024 / 1024 + "MB");

        assertThat(actual).hasSize(10_000);
    }
}
