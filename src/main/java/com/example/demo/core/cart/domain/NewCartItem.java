package com.example.demo.core.cart.domain;

import jakarta.validation.constraints.NotNull;

public record NewCartItem(@NotNull Long userId,
                          @NotNull Long productId,
                          @NotNull Long quantity,
                          @NotNull String codeA) {
}
