package com.citizenv.app.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException{
    String resourceName;
    String fieldName;
    long fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, long fieldValue) {
        super(String.format("%s not found with %s : %s\n", resourceName, fieldName, fieldValue));
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.resourceName = resourceName;
    }
}