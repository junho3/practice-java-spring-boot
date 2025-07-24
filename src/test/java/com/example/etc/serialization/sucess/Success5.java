package com.example.etc.serialization.sucess;

public class Success5 {

    private final String values;

    public static class Book {
        private final int value1;
        private final int value2;

        public Book(int value1, int value2) {
            this.value1 = value1;
            this.value2 = value2;
        }
    }

    public Success5(String values) {
        this.values = values;
    }

    public String getValues() {
        System.out.println("getValues() 호출");
        return values;
    }
}
