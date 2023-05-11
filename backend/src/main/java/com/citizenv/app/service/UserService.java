package com.citizenv.app.service;

import com.citizenv.app.payload.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();
    UserDto getById(String userId);
    UserDto createUser(UserDto userDto);
    UserDto updateUser(UserDto userDto);


}
