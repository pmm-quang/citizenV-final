package com.citizenv.app.exception;

public class ResourceFoundException extends ResourceException {
    public ResourceFoundException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s is already existed with %s : %s\n", resourceName, fieldName, fieldValue));
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.resourceName = resourceName;
    }
}
