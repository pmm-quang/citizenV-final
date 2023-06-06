package com.citizenv.app.service;

import com.citizenv.app.entity.User;
import com.citizenv.app.payload.DeclarationDto;

import java.util.List;

public interface DeclarationService {

    List<DeclarationDto> getAllByCreatedBy(Long  userDetailId);
    String setCompleted(String username);
    String openDeclaration(String username, DeclarationDto declaration);
    String lockDeclaration(String username);
    String changeTimeDeclaration(String username, DeclarationDto declaration);
}
