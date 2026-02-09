package com.example.demo.infrastructure.persistence.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ProductSalesRankingRedisRepositoryImpl implements ProductSalesRankingRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String KEY_PREFIX = "ranking:product-sales:";

    @Override
    public void save(final String productCode, final long salesQuantity, final LocalDate salesDate) {
        final String key = generateKey(salesDate);
        redisTemplate.opsForZSet().add(key, productCode, salesQuantity);
    }

    @Override
    public List<String> findTop10(final LocalDate salesDate) {
        final String key = generateKey(salesDate);
        final Set<Object> result = redisTemplate.opsForZSet().reverseRange(key, 0, 9);

        if (result == null) {
            return List.of();
        }

        return result.stream()
                .map(Object::toString)
                .toList();
    }

    @Override
    public LinkedHashMap<String, Long> findTop10WithScores(final LocalDate salesDate) {
        final String key = generateKey(salesDate);
        final Set<TypedTuple<Object>> result = redisTemplate.opsForZSet()
                .reverseRangeWithScores(key, 0, 9);

        if (result == null) {
            return new LinkedHashMap<>();
        }

        final LinkedHashMap<String, Long> rankings = new LinkedHashMap<>();
        for (final TypedTuple<Object> tuple : result) {
            final String productCode = tuple.getValue().toString();
            final Double score = tuple.getScore();
            rankings.put(productCode, score != null ? score.longValue() : 0L);
        }
        return rankings;
    }

    private String generateKey(final LocalDate salesDate) {
        return KEY_PREFIX + salesDate;
    }
}
