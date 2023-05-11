package com.citizenv.app.payload.custom;

import com.citizenv.app.payload.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private UserDto user;
}
