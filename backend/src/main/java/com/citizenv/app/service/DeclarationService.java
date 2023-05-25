package com.citizenv.app.service;

import com.citizenv.app.entity.User;
import com.citizenv.app.payload.DeclarationDto;

import java.util.List;

public interface DeclarationService {

    List<DeclarationDto> getAll();
    List<DeclarationDto> getAllByCreatedBy(Long  userDetailId);

    DeclarationDto createDeclaration(DeclarationDto declaration);
    DeclarationDto updateDeclaration(String username, DeclarationDto declaration);

    boolean hasDeclarationRights(String username);
    void lockDeclarationRights(User user);
    void lockDeclarationRights();
    void unlockDeclarationRights(User user);
    void unlockDeclarationRights();

    DeclarationDto setCompleted(String username);

    DeclarationDto openDeclaration(String username, DeclarationDto declaration);
    void lockDeclaration(String username);
}
