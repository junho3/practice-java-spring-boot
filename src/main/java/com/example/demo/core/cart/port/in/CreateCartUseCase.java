package com.example.demo.core.cart.port.in;

public interface CreateCartUseCase {

    void create(CreateCartCommand command);

    void createWithLogic(CreateCartCommand command);
}
