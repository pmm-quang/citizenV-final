package com.citizenv.app.controller;

import com.citizenv.app.payload.AdministrativeDivisionDto;
import com.citizenv.app.payload.UserDto;
import com.citizenv.app.payload.login.LoginRequest;
import com.citizenv.app.payload.login.LoginResponse;
import com.citizenv.app.secirity.CustomUserDetail;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final ModelMapper mapper;

    public AuthController(AuthenticationManager authenticationManager, ModelMapper mapper) {
        this.authenticationManager = authenticationManager;
        this.mapper = mapper;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
        UserDto userDto = mapper.map(userDetail.getUser(), UserDto.class);
        AdministrativeDivisionDto division = userDto.getDivision();
        String role = null;
        if (division != null) {
            String divisionCode = division.getCode();
            switch (divisionCode.length()) {
                case 2: role = "A2";break;
                case 4: role = "A3"; break;
                case 6: role = "B1"; break;
                case 8: role = "B2"; break;
            }
        } else {
            role = "A1";
        }
        LoginResponse info = new LoginResponse();
        info.setUsername(userDetail.getUsername());
        info.setRole(role);
        info.setDivision(division);
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

}
