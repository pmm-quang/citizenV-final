package com.citizenv.app.payload.login;

import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
