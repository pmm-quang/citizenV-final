package com.citizenv.app.controller;
import com.citizenv.app.payload.UserDto;
import com.citizenv.app.payload.request.ChangePasswordRequest;
import com.citizenv.app.secirity.SecurityUtils;
import com.citizenv.app.service.UserService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:3001/"})
@RestController
@RequestMapping("api/v1/user")
public class UserController {
//    @Autowired
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAll() {
//        CustomUserDetail userDetail = getUserDetail();
        String usernameUserDetail = SecurityUtils.getUsernameCurrentUserLogin();
        System.out.println(usernameUserDetail);
        List<UserDto> userDtoList = userService.getByCreatedBy(usernameUserDetail);
//        List<UserDto> userDtoList = userService.getAll();
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getById(@PathVariable String userId) {
//        CustomUserDetail userDetail = getUserDetail();
        UserDto userDto = userService.getById(userId);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @PutMapping("/change-password/{username}")
    public ResponseEntity<?> createNewPassword(@PathVariable String username, @RequestBody Map<String, String> request ) {
        String newPassword = request.get("newPassword");
        String userDetailUsername = SecurityUtils.getUsernameCurrentUserLogin();
        String message = userService.createNewPassword(userDetailUsername, username, newPassword);
        return ResponseEntity.ok(message);
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        String userDetailUsername = SecurityUtils.getUsernameCurrentUserLogin();
        return ResponseEntity.ok().body(userService.changePassword(userDetailUsername, request));
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @PostMapping("/save")
    public ResponseEntity<?> createUser(@RequestBody UserDto user) {
        String divisionUserDetail = SecurityUtils.getDivisionCodeCurrentUserLogin();
        String usernameUserDetail = SecurityUtils.getUsernameCurrentUserLogin();
        System.out.println("username: " + usernameUserDetail + ", divisionCode: " + divisionUserDetail);
        String message = userService.createUser(usernameUserDetail,divisionUserDetail, user);
        return ResponseEntity.status(201).body(message);
    }

}
