package com.example.etc.serialization;

import com.example.etc.serialization.sucess.Success1;
import com.example.etc.serialization.sucess.Success2;
import com.example.etc.serialization.sucess.Success3;
import com.example.etc.serialization.sucess.Success4;
import com.example.etc.serialization.sucess.Success5;
import com.example.etc.serialization.sucess.Success6;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class SerializationSuccessTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
    }


    @Test
    @DisplayName("record + inner record 조합: 문제없음")
    void test1() {
        final Success1 sut = new Success1("1.9,3.5,10.3");

        final String actual = assertDoesNotThrow(() -> objectMapper.writeValueAsString(sut));

        System.out.println(actual);
    }

    @Test
    @DisplayName("record + inner class 조합: 문제 없음")
    void test2() {
        final Success2 sut = new Success2("1.9,3.5,10.3");

        final String actual = assertDoesNotThrow(() -> objectMapper.writeValueAsString(sut));

        System.out.println(actual);
    }

    @Test
    @DisplayName("class + inner class + @JsonProperty 조합: 문제 없음")
    void test3() {
        final Success3 sut = new Success3("1.9,3.5,10.3");

        final String actual = assertDoesNotThrow(() -> objectMapper.writeValueAsString(sut));

        System.out.println(actual);
    }

    @Test
    @DisplayName("class + inner class + FAIL_ON_EMPTY_BEANS 옵션 조합: 문제 없음")
    void test4() {
        final Success4 sut = new Success4("1.9,3.5,10.3");
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        final String actual = assertDoesNotThrow(() -> objectMapper.writeValueAsString(sut));

        System.out.println(actual);
    }

    @Test
    @StdIo
    @DisplayName("class + get() + inner class 조합: 문제 없음")
    void test5(StdOut stdOut) {
        final Success5 sut = new Success5("1.9,3.5,10.3");

        final String actual = assertDoesNotThrow(() -> objectMapper.writeValueAsString(sut));

        assertThat(stdOut.capturedLines()[0]).isEqualTo("getValues() 호출");

        System.out.println(actual);
    }

    @Test
    @StdIo
    @DisplayName("record + inner record + @JsonIgnore 조합: 문제 없음")
    void test6(StdOut stdOut) {
        final Success6 sut = new Success6("1.9,3.5,10.3");

        final String actual = assertDoesNotThrow(() -> objectMapper.writeValueAsString(sut));

        assertThat(stdOut.capturedLines()).isEmpty();

        System.out.println(actual);
    }
}
