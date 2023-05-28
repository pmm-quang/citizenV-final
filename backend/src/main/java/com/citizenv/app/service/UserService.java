package com.citizenv.app.service;

import com.citizenv.app.payload.UserDto;
import com.citizenv.app.payload.request.ChangePasswordRequest;
import com.citizenv.app.secirity.CustomUserDetail;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();
    List<UserDto> getAll(CustomUserDetail userDetail);
    List<UserDto> getByCreatedBy(String usernameUserDetail);
    UserDto getById(String userId);
    String createUser(String usernameUserDetail, String divisionCodeUserDetail, UserDto userDto);

    String createNewPassword(String userDetailUsername, String username, String newPassword);

    String changePassword(String usernameUserDetail,ChangePasswordRequest request);



}
