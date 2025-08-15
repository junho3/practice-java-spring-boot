package com.example.demo.core.ai;

import java.io.Serial;

public class ExtractFileException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -909840593239145154L;

    public ExtractFileException(String message, Throwable e) {
        super(message, e);
    }
}
