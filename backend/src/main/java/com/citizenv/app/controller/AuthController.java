package com.citizenv.app.controller;

import com.citizenv.app.payload.UserDto;
import com.citizenv.app.payload.custom.LoginResponse;
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
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ModelMapper mapper;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, String> form) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(form.get("username"), form.get("password"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
//        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        LoginResponse loginResponse = new LoginResponse(userDto);
        Map<String, Object> info = new HashMap<>();
        info.put("username", authentication.getName());
        info.put("roles", authentication.getAuthorities());
        info.put("detail", authentication.getDetails());
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

}
