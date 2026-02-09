package com.example.demo.infrastructure.persistence.product;

import com.example.demo.annotation.RedisTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RedisTest
@ContextConfiguration(classes = ProductSalesRankingRedisRepositoryImpl.class)
class ProductSalesRankingRedisRepositoryImplTest {

    @Autowired
    private ProductSalesRankingRepository sut;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final LocalDate SALES_DATE = LocalDate.of(2025, 1, 6);

    @BeforeEach
    void setUp() {
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
    }

    @Nested
    @DisplayName("save 메소드는")
    class Describe_save {

        @Test
        @DisplayName("상품 판매량을 Redis Sorted Set에 저장한다.")
        void it_stores_product_sales_to_sorted_set() {
            // when
            sut.save("P001", 500, SALES_DATE);

            // then
            final Double actual = redisTemplate.opsForZSet()
                    .score("ranking:product-sales:" + SALES_DATE, "P001");
            assertThat(actual).isEqualTo(500.0);
        }

        @Test
        @DisplayName("동일 상품 저장 시 판매량이 업데이트된다.")
        void it_updates_sales_quantity() {
            // given
            sut.save("P001", 100, SALES_DATE);

            // when
            sut.save("P001", 500, SALES_DATE);

            // then
            final Double actual = redisTemplate.opsForZSet()
                    .score("ranking:product-sales:" + SALES_DATE, "P001");
            assertThat(actual).isEqualTo(500.0);
        }
    }

    @Nested
    @DisplayName("findTop10 메소드는")
    class Describe_findTop10 {

        @Nested
        @DisplayName("데이터가 존재할 때")
        class Context_data_exists {

            @BeforeEach
            void setUp() {
                sut.save("P001", 100, SALES_DATE);
                sut.save("P002", 300, SALES_DATE);
                sut.save("P003", 200, SALES_DATE);
            }

            @Test
            @DisplayName("판매량 내림차순으로 상품코드를 리턴한다.")
            void it_returns_product_codes_in_descending_order() {
                final List<String> actual = sut.findTop10(SALES_DATE);

                assertThat(actual).containsExactly("P002", "P003", "P001");
            }
        }

        @Nested
        @DisplayName("데이터가 없을 때")
        class Context_no_data {

            @Test
            @DisplayName("빈 리스트를 리턴한다.")
            void it_returns_empty_list() {
                final List<String> actual = sut.findTop10(SALES_DATE);

                assertThat(actual).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("findTop10WithScores 메소드는")
    class Describe_findTop10WithScores {

        @Nested
        @DisplayName("데이터가 존재할 때")
        class Context_data_exists {

            @BeforeEach
            void setUp() {
                sut.save("P001", 100, SALES_DATE);
                sut.save("P002", 300, SALES_DATE);
                sut.save("P003", 200, SALES_DATE);
            }

            @Test
            @DisplayName("판매량 내림차순으로 상품코드와 판매량을 리턴한다.")
            void it_returns_product_codes_with_scores_in_descending_order() {
                final LinkedHashMap<String, Long> actual = sut.findTop10WithScores(SALES_DATE);

                assertThat(actual).hasSize(3);
                assertThat(actual.keySet().toArray(new String[0]))
                        .containsExactly("P002", "P003", "P001");
                assertThat(actual.get("P002")).isEqualTo(300L);
                assertThat(actual.get("P003")).isEqualTo(200L);
                assertThat(actual.get("P001")).isEqualTo(100L);
            }
        }

        @Nested
        @DisplayName("데이터가 없을 때")
        class Context_no_data {

            @Test
            @DisplayName("빈 맵을 리턴한다.")
            void it_returns_empty_map() {
                final LinkedHashMap<String, Long> actual = sut.findTop10WithScores(SALES_DATE);

                assertThat(actual).isEmpty();
            }
        }
    }
}
