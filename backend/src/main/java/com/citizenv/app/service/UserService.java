package com.citizenv.app.service;

import com.citizenv.app.payload.UserDto;
import com.citizenv.app.secirity.CustomUserDetail;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();
    List<UserDto> getAll(CustomUserDetail userDetail);
    List<UserDto> getByCreatedBy(String usernameUserDetail);
    UserDto getById(String userId);
    UserDto createUser(CustomUserDetail userDetail, UserDto userDto);
    UserDto createUser(String usernameUserDetail, String divisionCodeUserDetail, UserDto userDto);
    UserDto createUser(UserDto userDto);
    UserDto updateUser(String divisionCodeUserDetail, UserDto userDto);

    UserDto changePassword(String userDetailUsername, String username, String newPassword);


}
