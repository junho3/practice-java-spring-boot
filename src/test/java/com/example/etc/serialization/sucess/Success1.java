package com.example.etc.serialization.sucess;

public record Success1(
    String values
) {
    public record Book(
        int value1,
        int value2
    ) {}
}
