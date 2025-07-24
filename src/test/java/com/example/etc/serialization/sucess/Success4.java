package com.example.etc.serialization.sucess;

public class Success4 {

    private final String values;

    public static class Book {
        private final int value1;
        private final int value2;

        public Book(int value1, int value2) {
            this.value1 = value1;
            this.value2 = value2;
        }
    }

    public Success4(String values) {
        this.values = values;
    }
}
