package com.example.demo.core.cart.port.in;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateCartCommand(@Size(min = 1) List<CartItemVO> cartItemVOs,
                                @NotNull Long userId) {
}
