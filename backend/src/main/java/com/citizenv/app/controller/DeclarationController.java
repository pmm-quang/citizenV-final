package com.citizenv.app.controller;

import com.citizenv.app.payload.DeclarationDto;
import com.citizenv.app.secirity.SecurityUtils;
import com.citizenv.app.service.DeclarationService;
import com.citizenv.app.service.impl.DeclarationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PutMapping("/save/{username}")
    public ResponseEntity<DeclarationDto> update(@PathVariable String username,
                                                 @RequestBody DeclarationDto declarationDto) {
        DeclarationDto dto = declarationService.openDeclaration(username, declarationDto);
        return ResponseEntity.status(201).body(dto);
    }

    @GetMapping("/lock-declaration/{username}")
    public ResponseEntity<String> lockDeclaration(@PathVariable String username) {
        declarationService.lockDeclaration(username);
        return ResponseEntity.ok().body("Đã khóa quyền khai báo");
    }

    @GetMapping("/set-completed")
    public ResponseEntity<?> setDeclarationCompleted() {
        String usernameUserDetail = SecurityUtils.getUsernameCurrentUserLogin();
        declarationService.setCompleted(usernameUserDetail);
        return ResponseEntity.ok().body("Đã hòan thành khai báo");
    }
}
