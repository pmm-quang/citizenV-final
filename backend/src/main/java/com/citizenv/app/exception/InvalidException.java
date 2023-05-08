package com.citizenv.app.exception;

import com.citizenv.app.payload.error.ErrorResponse;

public class InvalidException extends RuntimeException{
    public InvalidException(String message) {
        super(String.format(message));
    }
}
