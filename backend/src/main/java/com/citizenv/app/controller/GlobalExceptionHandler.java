package com.citizenv.app.controller;

import com.citizenv.app.component.Constant;
import com.citizenv.app.exception.InvalidException;
import com.citizenv.app.exception.ResourceFoundException;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.error.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
    public ResponseEntity<String> validateException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> accessDenied(Exception e) {
        String message = e.getMessage();
        if(e.getMessage().equals("ACCESS DENIED")) {
            message = Constant.ACCESS_DENIED_MESSAGE_DO_NOT_HAVE_ACCESS;
        }
        return ResponseEntity.status(403).body(message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnwantedException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("Bố mày không biết lỗi gì");
    }
}