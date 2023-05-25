package com.citizenv.app.controller;

import com.citizenv.app.payload.AdministrativeDivisionDto;
import com.citizenv.app.payload.UserDto;
import com.citizenv.app.payload.login.LoginRequest;
import com.citizenv.app.payload.login.LoginResponse;
import com.citizenv.app.secirity.CustomUserDetail;
import com.citizenv.app.secirity.jwt.JwtFilter;
import com.citizenv.app.secirity.jwt.JwtTokenProvider;
import org.modelmapper.ModelMapper;
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
    private final JwtTokenProvider tokenProvider;

    public AuthController(AuthenticationManager authenticationManager, ModelMapper mapper, JwtFilter jwtFilter, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.mapper = mapper;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
        String jwt = tokenProvider.generateToken(userDetail);
        UserDto userDto = mapper.map(userDetail.getUser(), UserDto.class);
        AdministrativeDivisionDto division = userDto.getDivision();
        String declarationStatus = null;
        String role = null;
        if (division != null) {
            String divisionCode = division.getCode();
            switch (divisionCode.length()) {
                case 2: role = "A2";break;
                case 4: role = "A3"; break;
                case 6: role = "B1"; break;
                case 8: role = "B2"; break;
            }
            declarationStatus = userDetail.getUser().getDeclaration().getStatus();
        } else {
            role = "A1";
        }
        LoginResponse info = new LoginResponse();
        info.setUsername(userDetail.getUsername());
        info.setRole(role);
        info.setDivision(division);
        info.setDeclarationStatus(declarationStatus);
        info.setAccessToken(jwt);
//        Map<String, Object> map = new HashMap<>();
//        map.put("info", info);
//        map.put("accessToken", jwt);
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

}
