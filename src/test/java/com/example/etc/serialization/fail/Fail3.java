package com.example.etc.serialization.fail;

public record Fail3(
    String values
) {
    public record Book(
        int value1,
        int value2
    ) {}

    public Book getBook() {
        String[] data = values.split(",");

        System.out.println("get() 호출");

        return new Book(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
    }
}
