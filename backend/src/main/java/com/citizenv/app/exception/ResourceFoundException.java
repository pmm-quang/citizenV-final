package com.citizenv.app.exception;

public class ResourceFoundException extends RuntimeException {

    public ResourceFoundException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s với %s: %s đã tồn tại\n", resourceName, fieldName, fieldValue));

    }
}
