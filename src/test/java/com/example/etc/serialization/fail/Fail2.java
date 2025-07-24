package com.example.etc.serialization.fail;

import java.io.Serial;
import java.io.Serializable;

public record Fail2(
    String values
) implements Serializable {
    @Serial
    private static final long serialVersionUID = -1446398935944895849L;

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
