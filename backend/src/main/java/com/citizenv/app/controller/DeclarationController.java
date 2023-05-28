package com.citizenv.app.controller;

import com.citizenv.app.payload.DeclarationDto;
import com.citizenv.app.secirity.SecurityUtils;
import com.citizenv.app.service.DeclarationService;
import com.citizenv.app.service.impl.DeclarationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:3001/"})
@RestController
@RequestMapping("api/v1/declaration")
public class DeclarationController {
    private final DeclarationService declarationService;

    public DeclarationController(DeclarationService declarationService) {
        this.declarationService = declarationService;
    }

    @GetMapping("/")
    public ResponseEntity<List<DeclarationDto>> getAll() {
        Long userDetailId = SecurityUtils.getIdCurrentUserLogin();
        System.out.println(userDetailId);
        List<DeclarationDto> declarationDtoList = declarationService.getAllByCreatedBy(userDetailId);
        return new ResponseEntity<List<DeclarationDto>>(declarationDtoList, HttpStatus.OK);
    }

    /*@GetMapping("/{provinceId}")
    public ResponseEntity<ProvinceDto> getById(@PathVariable String provinceId) {
        ProvinceDto provinceDto = declarationService.getById(provinceId);
        return new ResponseEntity<>(provinceDto, HttpStatus.OK);
    }*/
    @PreAuthorize("hasAuthority('WRITE')")
    @PutMapping("/save/{username}")
    public ResponseEntity<?> update(@PathVariable String username,
                                                 @RequestBody DeclarationDto declarationDto) {
        String message = declarationService.openDeclaration(username, declarationDto);
        return ResponseEntity.status(201).body(message);
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @GetMapping("/lock-declaration/{username}")
    public ResponseEntity<String> lockDeclaration(@PathVariable String username) {
        String message = declarationService.lockDeclaration(username);
        return ResponseEntity.ok().body(message);
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @GetMapping("/set-completed")
    public ResponseEntity<?> setDeclarationCompleted() {
        String usernameUserDetail = SecurityUtils.getUsernameCurrentUserLogin();
        String message = declarationService.setCompleted(usernameUserDetail);
        return ResponseEntity.ok().body(message);
    }
}
