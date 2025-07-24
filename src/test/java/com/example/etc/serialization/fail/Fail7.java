package com.example.etc.serialization.fail;

public class Fail7 {

    private final String values;

    public static class Book {
        private final int value1;
        private final int value2;

        public Book(int value1, int value2) {
            this.value1 = value1;
            this.value2 = value2;
        }
    }

    public Fail7(String values) {
        this.values = values;
    }
}
