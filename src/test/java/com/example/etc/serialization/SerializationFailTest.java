package com.example.etc.serialization;

import com.example.etc.serialization.fail.Fail1;
import com.example.etc.serialization.fail.Fail2;
import com.example.etc.serialization.fail.Fail3;
import com.example.etc.serialization.fail.Fail4;
import com.example.etc.serialization.fail.Fail5;
import com.example.etc.serialization.fail.Fail6;
import com.example.etc.serialization.fail.Fail7;
import com.example.etc.serialization.fail.Fail8;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SerializationFailTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @StdIo
    @DisplayName("record + inner record + get() 조합: get()를 호출하면서 직렬화 시도")
    void test1(StdOut stdOut) {
        final Fail1 sut = new Fail1("1.9,3.5,10.3");

        assertThatThrownBy(() -> objectMapper.writeValueAsString(sut))
            .isExactlyInstanceOf(JsonMappingException.class);

        assertThat(stdOut.capturedLines()[0]).isEqualTo("get() 호출");
    }

    @Test
    @StdIo
    @DisplayName("record + inner record + get() 조합: get()를 호출하면서 직렬화 시도, inner record 명인 'Package' 문제는 아님")
    void test2(StdOut stdOut) {
        final Fail2 sut = new Fail2("1.9,3.5,10.3");

        assertThatThrownBy(() -> objectMapper.writeValueAsString(sut))
            .isExactlyInstanceOf(JsonMappingException.class);

        assertThat(stdOut.capturedLines()[0]).isEqualTo("get() 호출");
    }

    @Test
    @StdIo
    @DisplayName("record + inner record + get() 조합: get()를 호출하면서 직렬화 시도, 'Serializable' 구현 문제는 아님")
    void test3(StdOut stdOut) {
        final Fail3 sut = new Fail3("1.9,3.5,10.3");

        assertThatThrownBy(() -> objectMapper.writeValueAsString(sut))
            .isExactlyInstanceOf(JsonMappingException.class);

        assertThat(stdOut.capturedLines()[0]).isEqualTo("get() 호출");
    }

    @Test
    @StdIo
    @DisplayName("class + inner record + get() 조합: get()를 호출하면서 직렬화 시도")
    void test4(StdOut stdOut) {
        final Fail4 sut = new Fail4("1.9,3.5,10.3");

        assertThatThrownBy(() -> objectMapper.writeValueAsString(sut))
            .isExactlyInstanceOf(JsonMappingException.class);

        assertThat(stdOut.capturedLines()[0]).isEqualTo("get() 호출");
    }

    @Test
    @DisplayName("class + inner record 조합: values 필드의 getter가 없어서 직렬화 실패")
    void test5() {
        final Fail5 sut = new Fail5("1.9,3.5,10.3");

        assertThatThrownBy(() -> objectMapper.writeValueAsString(sut))
            .isExactlyInstanceOf(InvalidDefinitionException.class);
    }

    @Test
    @StdIo
    @DisplayName("class + inner class + get() 조합: get()를 호출하면서 inner class부터 직렬화 시도")
    void test6(StdOut stdOut) {
        final Fail6 sut = new Fail6("1.9,3.5,10.3");

        assertThatThrownBy(() -> objectMapper.writeValueAsString(sut))
            .isExactlyInstanceOf(JsonMappingException.class);

        assertThat(stdOut.capturedLines()[0]).isEqualTo("get() 호출");
    }

    @Test
    @DisplayName("class + inner class 조합: values 필드의 getter가 없어서 직렬화 실패")
    void test7() {
        final Fail7 sut = new Fail7("1.9,3.5,10.3");

        assertThatThrownBy(() -> objectMapper.writeValueAsString(sut))
            .isExactlyInstanceOf(InvalidDefinitionException.class);
    }


    @Test
    @StdIo
    @DisplayName("class + inner class 조합 + get() 조합: values 필드의 getter가 없어서 직렬화 실패, inner class의 get()을 호출하진 않음")
    void test8(StdOut stdOut) {
        final Fail8 sut = new Fail8("1.9,3.5,10.3");

        assertThatThrownBy(() -> objectMapper.writeValueAsString(sut))
            .isExactlyInstanceOf(InvalidDefinitionException.class);

        assertThat(stdOut.capturedLines()).isEmpty();
    }
}
