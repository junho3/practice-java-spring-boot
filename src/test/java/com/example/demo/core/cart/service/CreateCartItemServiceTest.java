package com.example.demo.core.cart.service;

import com.example.demo.core.cart.domain.CartItem;
import com.example.demo.core.cart.port.in.CreateCartCommand;
import com.example.demo.core.cart.port.out.CreateCartItemPort;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateCartItemService")
class CreateCartItemServiceTest {

    @Mock
    private CreateCartItemPort createCartItemPort;
    @InjectMocks
    private CreateCartItemService createCartItemService;

    final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
        .plugin(new JakartaValidationPlugin())
        .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
        .build();

    @Test
    @DisplayName("create()는 Cart를 저장한다.")
    void create_save_cart() {
        final CreateCartCommand command = fixtureMonkey.giveMeOne(CreateCartCommand.class);

        doNothing().when(createCartItemPort).createOrUpdates(anyList(), eq(command.userId()));

        createCartItemService.create(command);

        verify(createCartItemPort, times(1)).createOrUpdates(anyList(), eq(command.userId()));
    }
}
