package com.example.demo.core.cart.domain;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CartItem(Long cartItemId,
                       @NotNull Long userId,
                       @NotNull Long productId,
                       @NotNull Long quantity,
                       @NotNull String codeA,
                       String codeB,
                       LocalDateTime createdAt) {
}
