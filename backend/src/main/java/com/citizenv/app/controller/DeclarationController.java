package com.citizenv.app.controller;

import com.citizenv.app.payload.DeclarationDto;
import com.citizenv.app.service.impl.DeclarationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("api/v1/declaration")
public class DeclarationController {
    @Autowired
    DeclarationServiceImpl declarationService;

    @GetMapping("/")
    public ResponseEntity<List<DeclarationDto>> getAll() {
        List<DeclarationDto> declarationDtoList = declarationService.getAll();
        return new ResponseEntity<List<DeclarationDto>>(declarationDtoList, HttpStatus.OK);
    }

    /*@GetMapping("/{provinceId}")
    public ResponseEntity<ProvinceDto> getById(@PathVariable String provinceId) {
        ProvinceDto provinceDto = declarationService.getById(provinceId);
        return new ResponseEntity<>(provinceDto, HttpStatus.OK);
    }*/
}
