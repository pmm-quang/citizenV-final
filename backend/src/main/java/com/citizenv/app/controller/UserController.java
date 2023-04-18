package com.citizenv.app.controller;
import com.citizenv.app.payload.UserDto;
import com.citizenv.app.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/user")
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAll() {
        List<UserDto> userDtoList = userService.getAll();
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getById(@PathVariable String userId) {
        UserDto userDto = userService.getById(userId);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<UserDto> create(@RequestBody Map<String, Object> provinceJSONInfo) {
        UserDto userDto = userService.createUser(provinceJSONInfo);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<UserDto> update(@RequestBody Map<String, Object> provinceJSONInfo) throws NoSuchMethodException {
        UserDto userDto = userService.updateUser(provinceJSONInfo);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteById(@PathVariable String userId) {
        return new ResponseEntity<>(userService.deleteById(userId), HttpStatus.OK);
    }
}
