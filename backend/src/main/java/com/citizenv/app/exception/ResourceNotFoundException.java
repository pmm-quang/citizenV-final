package com.citizenv.app.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s với %s : %s đã tồn tại\n", resourceName, fieldName, fieldValue));
    }
}