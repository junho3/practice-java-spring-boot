package com.example.demo.core.cart.port.in;

import java.util.List;

public record CreateCartCommand(List<CartItemVO> cartItemVOs,
                                Long userId) {
}
