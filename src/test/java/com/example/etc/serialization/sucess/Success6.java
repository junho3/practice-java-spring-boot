package com.example.etc.serialization.sucess;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record Success6(
    String values
) {
    public record Book(
        int value1,
        int value2
    ) {}

    @JsonIgnore
    public Book getBook() {
        String[] data = values.split(",");

        System.out.println("get() 호출");

        return new Book(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
    }
}
