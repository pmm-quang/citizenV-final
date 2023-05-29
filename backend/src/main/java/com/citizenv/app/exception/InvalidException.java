package com.citizenv.app.exception;

public class InvalidException extends RuntimeException {
    public InvalidException(String message) {
        super(String.format(message));
    }
}
