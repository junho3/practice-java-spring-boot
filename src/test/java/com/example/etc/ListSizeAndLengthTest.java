package com.example.etc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("ListSizeAndLengthTest")
class ListSizeAndLengthTest {

    @Test
    void compareSizeAndLength() {
        final List<String> items = List.of("a", "b", "c", "d", "e");


        System.out.println(items.size());
        System.out.println(items.toArray().length);

        assertEquals(items.size(), items.toArray().length);
    }
}
