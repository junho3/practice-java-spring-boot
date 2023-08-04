package com.example.demo.annotation;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(MockitoExtension.class)
@TestEnvironment
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MockTest {
}
