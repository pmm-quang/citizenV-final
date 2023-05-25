package com.citizenv.app.payload.login;

import com.citizenv.app.payload.AdministrativeDivisionDto;
import com.citizenv.app.payload.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//@AllArgsConstructor
public class LoginResponse {
    private String username;
    private String role;
    private String declarationStatus;
    private AdministrativeDivisionDto division;
    private String accessToken;
}
