package com.example.demo.core.chat;

import java.io.Serial;

public class ChatProviderException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1137714761707475044L;

    public ChatProviderException(String message) {
        super(message);
    }
}
