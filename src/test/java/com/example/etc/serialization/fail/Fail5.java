package com.example.etc.serialization.fail;

public class Fail5 {

    private final String values;

    public record Book(
        int value1,
        int value2
    ) {}

    public Fail5(String values) {
        this.values = values;
    }
}
