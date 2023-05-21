package com.citizenv.app.service;

import com.citizenv.app.entity.User;
import com.citizenv.app.payload.DeclarationDto;

import java.util.List;

public interface DeclarationService {

    List<DeclarationDto> getAll();

    DeclarationDto createDeclaration(DeclarationDto declaration);
    DeclarationDto updateDeclaration(String username, DeclarationDto declaration);

    boolean hasDeclarationRights(String username);
    void lockDeclarationRights(User user);
    void lockDeclarationRights();
    void unlockDeclarationRights(User user);
    void unlockDeclarationRights();
}
