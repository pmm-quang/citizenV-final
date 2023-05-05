package com.citizenv.app.exception;

public class ResourceException extends RuntimeException {
    protected String resourceName;
    protected String fieldName;
    protected String fieldValue;

    public ResourceException(String resourceName, String fieldName, String fieldValue) {
        super();
    }

    public ResourceException(String format) {
        super(format);
    }
}
