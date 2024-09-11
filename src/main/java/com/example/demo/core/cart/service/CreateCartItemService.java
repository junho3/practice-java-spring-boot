package com.example.demo.core.cart.service;

import com.example.demo.core.cart.port.in.CreateCartCommand;
import com.example.demo.core.cart.port.in.CreateCartUseCase;
import com.example.demo.core.cart.port.out.CreateCartItemPort;
import org.springframework.stereotype.Service;

@Service
public class CreateCartItemService implements CreateCartUseCase {

    private final CreateCartItemPort createCartItemPort;

    public CreateCartItemService(CreateCartItemPort createCartItemPort) {
        this.createCartItemPort = createCartItemPort;
    }

    @Override
    public void create(CreateCartCommand command) {

    }

    @Override
    public void createWithLogic(CreateCartCommand command) {

    }
}
