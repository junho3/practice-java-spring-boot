package com.example.etc.serialization.fail;

public class Fail6 {

    private final String values;

    public static class Book {
        private final int value1;
        private final int value2;

        public Book(int value1, int value2) {
            this.value1 = value1;
            this.value2 = value2;
        }
    }

    public Book getBook() {
        String[] data = values.split(",");

        System.out.println("get() 호출");

        return new Book(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
    }

    public Fail6(String values) {
        this.values = values;
    }
}
