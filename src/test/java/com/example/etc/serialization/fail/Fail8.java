package com.example.etc.serialization.fail;

public class Fail8 {

    private final String values;

    public static class Book {
        private final int value1;
        private final int value2;

        public Book(int value1, int value2) {
            this.value1 = value1;
            this.value2 = value2;
        }

        public int getValue1() {
            System.out.println("getValue1() 호출");
            return value1;
        }

        public int getValue2() {
            System.out.println("getValue2() 호출");
            return value2;
        }
    }

    public Fail8(String values) {
        this.values = values;
    }
}
