package com.citizenv.app.controller;
import com.citizenv.app.payload.UserDto;
import com.citizenv.app.secirity.CustomUserDetail;
import com.citizenv.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
//        List<UserDto> userDtoList = userService.getAll(userDetail);
        List<UserDto> userDtoList = userService.getAll();
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getById(@PathVariable String userId) {
//        CustomUserDetail userDetail = getUserDetail();
        UserDto userDto = userService.getById(userId);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

//    @PutMapping("/change-password/{username}")
//    public ResponseEntity<UserDto> changePassword(@PathVariable String username, @RequestBody String newPassword ) {
//        CustomUserDetail userDetail = getUserDetail();
//        String userDetailUsername = userDetail.getUsername();
//        UserDto userDto = userService.changePassword(userDetailUsername, username, newPassword);
//        return ResponseEntity.ok(userDto);
//    }

    @PostMapping("/save")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto user) {
//        CustomUserDetail userDetail = getUserDetail();
//        UserDto userDto = userService.createUser(userDetail, user);
        UserDto userDto = userService.createUser(user);
        return ResponseEntity.status(201).body(userDto);

    }

//    @PostMapping("/")
//    public ResponseEntity<UserDto> create(@RequestBody Map<String, Object> provinceJSONInfo) {
//        UserDto userDto = userService.createUser(provinceJSONInfo);
//        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
//    }

//    @PutMapping("/")
//    public ResponseEntity<UserDto> update(@RequestBody Map<String, Object> provinceJSONInfo) throws NoSuchMethodException {
//        UserDto userDto = userService.updateUser(provinceJSONInfo);
//        return new ResponseEntity<>(userDto, HttpStatus.OK);
//    }

//    @DeleteMapping("/{userId}")
//    public ResponseEntity<String> deleteById(@PathVariable String userId) {
//        return new ResponseEntity<>(userService.deleteById(userId), HttpStatus.OK);
//    }

//    private CustomUserDetail getUserDetail() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
//        return userDetail;
//    }
}
