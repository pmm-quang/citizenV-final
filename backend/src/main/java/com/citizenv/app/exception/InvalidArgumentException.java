package com.citizenv.app.exception;

public class InvalidArgumentException extends IllegalArgumentException {

    public InvalidArgumentException(String message) {
        super(String.format(message));
    }
}
