package com.example.demo.core.product.service;

import com.example.demo.TestDataInsertSupport;
import com.example.demo.TestFixtures;
import com.example.demo.annotation.IntegrationTest;
import com.example.demo.core.product.domain.ProductSales;
import com.example.demo.core.product.param.FindProductSalesTop10Param;
import com.example.demo.core.product.result.FindProductSalesResult;
import com.example.demo.infrastructure.persistence.product.ProductSalesRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static io.github.resilience4j.circuitbreaker.CircuitBreaker.State.OPEN;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FindProductSalesService")
@IntegrationTest
@RequiredArgsConstructor
class FindProductSalesServiceTest extends TestDataInsertSupport {

    private final ProductSalesRepository productSalesRepository;
    private final FindProductSalesService findProductSalesService;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    @AfterEach
    void tearDown() {
        productSalesRepository.deleteAll();
    }

    @Nested
    @DisplayName("top10()은")
    class Describe_top10 {

        @Nested
        @DisplayName("익셉션이 발생하지 않았을 때")
        class Context_not_thrown_exception {

            @BeforeEach
            void setUp() {
                final List<ProductSales> productSales = TestFixtures.get()
                    .giveMeBuilder(ProductSales.class)
                    .setNull("id")
                    .setNotNull("productCode")
                    .setNotNull("productName")
                    .set("salesDate", LocalDate.of(2025, 1, 6))
                    .setLazy("salesQuantity", List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L).iterator()::next)
                    .sampleList(11);

                saveAll(productSales);
            }

            @Test
            @DisplayName("판매날짜 조건으로 판매량이 가장 높은 상품 10개를 리턴한다.")
            void then_return_highest_saleQuantity_product() {
                final FindProductSalesTop10Param param = new FindProductSalesTop10Param(LocalDate.of(2025, 1, 6));

                final List<FindProductSalesResult> actual = findProductSalesService.top10(param);

                assertThat(actual).hasSize(10);
                assertThat(actual.getFirst().salesQuantity()).isEqualTo(11);
            }
        }

        @Nested
        @DisplayName("익셉션이 계속 발생했을 때")
        class Context_thrown_exception {

            final CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("product-sales-top10");

            @AfterEach
            void tearDown() {
                circuitBreaker.reset();
            }

            @Test
            @DisplayName("서킷이 열리고 fallback을 리턴한다.")
            void then_open_circuit_return_fallback() {

                final FindProductSalesTop10Param param = new FindProductSalesTop10Param(LocalDate.of(2030, 1, 6));
                for (int i = 0; i < 5; i++) {
                    findProductSalesService.top10(param);
                }

                assertThat(circuitBreaker.getState()).isEqualTo(OPEN);
            }
        }
    }
}
