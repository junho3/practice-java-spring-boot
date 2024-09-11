package com.example.demo.core.cart.port.out;

import com.example.demo.core.cart.domain.CartItem;
import com.example.demo.core.cart.domain.NewCartItem;

import java.util.List;

public interface CreateCartItemPort {

    void create(List<CartItem> cartItems);

    void createOrUpdate(List<NewCartItem> newCartItems, Long userId);
}
