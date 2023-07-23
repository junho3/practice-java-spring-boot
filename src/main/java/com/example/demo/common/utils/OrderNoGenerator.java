package com.example.demo.common.utils;

import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class OrderNoGenerator {
    private static final int LENGTH_10_INT_RADIX = 9;

    // TODO Redis 등을 활용하여 멀티 환경 고민 필요
    public String generate() {
        int l = ByteBuffer
            .wrap(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8))
            .getInt();

        return Integer.toString(l, LENGTH_10_INT_RADIX);
    }
}
