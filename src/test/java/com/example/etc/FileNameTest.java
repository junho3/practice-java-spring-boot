package com.example.etc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("FileNameTest")
class FileNameTest {

    @Test
    @DisplayName("File 객체로 파일명을 가져오는 테스트 코드")
    void getFileNameTest() {
        final String inputFilePathNm = "/IdeaProjects/practice-java-spring-boot/image.png";

        final File file = new File(inputFilePathNm);
        final String fullFileName = file.getName();
        final String fileName = file.getName().replace(".png", "");

        assertEquals(fullFileName, "image.png");
        assertEquals(fileName, "image");
    }
}
