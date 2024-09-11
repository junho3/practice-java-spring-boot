package com.example.demo.core.cart.service;

import com.example.demo.core.cart.domain.CartItem;
import com.example.demo.core.cart.port.in.CreateCartCommand;
import com.example.demo.core.cart.port.out.CreateCartItemPort;
import com.example.demo.core.cart.port.out.FindCartItemPort;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateCartItemWithLogicService")
class CreateCartItemWithLogicServiceTest {

    @Mock
    private FindCartItemPort findCartItemPort;
    @Mock
    private CreateCartItemPort createCartItemPort;
    @InjectMocks
    private CreateCartItemWithLogicService createCartItemWithLogicService;

    final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
        .plugin(new JakartaValidationPlugin())
        .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
        .build();

    @Test
    @DisplayName("create()는 카트 조회 후 카트를 저장한다.")
    void create_when_find_cart_save_cart() {
        final CreateCartCommand command = fixtureMonkey.giveMeOne(CreateCartCommand.class);
        final List<CartItem> cartItems = fixtureMonkey.giveMeBuilder(CartItem.class).sampleList(1);

        given(findCartItemPort.findAll(command.userId())).willReturn(cartItems);
        doNothing().when(createCartItemPort).create(any());

        createCartItemWithLogicService.create(command);

        verify(findCartItemPort, times(1)).findAll(command.userId());
        verify(createCartItemPort, atLeastOnce()).create(any());
    }
}
