package com.citizenv.app.controller;

import com.citizenv.app.exception.InvalidException;
import com.citizenv.app.exception.ResourceFoundException;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
//@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:3001/"})
@RestControllerAdvice
public class  GlobalExceptionHandler {
    @ExceptionHandler({ ResourceNotFoundException.class, ResourceFoundException.class })
    public ResponseEntity<String> handleResourceException(Exception e) {
        return ResponseEntity.status(500).body(e.getMessage());
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> invalidUserOrPassword(Exception e) {
        return ResponseEntity.status(401).body("invalid username or password");
    }

    @ExceptionHandler(InvalidException.class)
    public ResponseEntity<ErrorResponse> validateException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> accessDenied(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(), e.getMessage());
        return ResponseEntity.status(403).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnwantedException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("Unknown error (General exception)");
    }
}