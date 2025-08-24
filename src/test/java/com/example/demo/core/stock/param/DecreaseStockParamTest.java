package com.example.demo.core.stock.param;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DecreaseStockParam")
class DecreaseStockParamTest {

    @Nested
    @DisplayName("동일한 productCode를 가진 Stock 객체는")
    class SameProductCodeTest {

        private final DecreaseStockParam.Stock stock1 = new DecreaseStockParam.Stock("P001", 3L);
        private final DecreaseStockParam.Stock stock2 = new DecreaseStockParam.Stock("P001", 5L);

        @Test
        @DisplayName("동등성 비교 시 true를 반환한다")
        void equals_returnsTrue() {
            assertThat(stock1).isEqualTo(stock2);
        }

        @Test
        @DisplayName("동일한 해시코드를 가진다")
        void hashCode_areSame() {
            assertThat(stock1.hashCode()).isEqualTo(stock2.hashCode());
        }

        @Test
        @DisplayName("Set에 추가할 때 중복으로 간주되어 하나만 저장된다")
        void set_containsOnlyOneElement() {
            // when
            Set<DecreaseStockParam.Stock> stockSet = new HashSet<>(List.of(stock1, stock2));

            // then
            assertThat(stockSet).hasSize(1);
            assertThat(stockSet).containsExactlyInAnyOrder(stock1);
        }
    }

    @Nested
    @DisplayName("다른 productCode를 가진 Stock 객체는")
    class DifferentProductCodeTest {

        private final DecreaseStockParam.Stock stock1 = new DecreaseStockParam.Stock("P001", 3L);
        private final DecreaseStockParam.Stock stock2 = new DecreaseStockParam.Stock("P002", 3L);

        @Test
        @DisplayName("동등성 비교 시 false를 반환한다")
        void equals_returnsFalse() {
            assertThat(stock1).isNotEqualTo(stock2);
        }

        @Test
        @DisplayName("다른 해시코드를 가질 수 있다 (해시 충돌 가능성은 있지만, 다른 객체임을 보장)")
        void hashCode_areDifferent() {
            assertThat(stock1.hashCode()).isNotEqualTo(stock2.hashCode());
        }

        @Test
        @DisplayName("Set에 추가할 때 모두 저장된다")
        void set_containsBothElements() {
            // when
            Set<DecreaseStockParam.Stock> stockSet = Set.of(stock1, stock2);

            // then
            assertThat(stockSet).hasSize(2);
            assertThat(stockSet).containsExactlyInAnyOrder(stock1, stock2);
        }
    }

    @Nested
    @DisplayName("DecreaseStockParam 생성 시")
    class DecreaseStockParamConstructorTest {

        @Test
        @DisplayName("동일한 productCode를 가진 Stock은 중복 제거되어 저장된다")
        void removesDuplicateProductCode() {
            // given
            var stock1 = new DecreaseStockParam.Stock("P001", 3L);
            var stock2 = new DecreaseStockParam.Stock("P001", 5L);
            var stock3 = new DecreaseStockParam.Stock("P002", 1L);

            // when
            var param = new DecreaseStockParam(new HashSet<>(List.of(stock1, stock2, stock3)));

            // then
            assertThat(param.stocks()).hasSize(2);
            assertThat(param.stocks()).containsExactlyInAnyOrder(stock1, stock3);
            // 마지막에 추가된 동일 productCode의 Stock이 유지됨 (Set의 특성상 순서는 보장되지 않음)
        }
    }
}
