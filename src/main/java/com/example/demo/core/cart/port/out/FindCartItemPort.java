package com.example.demo.core.cart.port.out;

import com.example.demo.core.cart.domain.CartItem;

import java.util.List;

public interface FindCartItemPort {

    List<CartItem> findAll(Long userId);
}
