package com.citizenv.app.controller;

import com.citizenv.app.payload.ProvinceDto;
import com.citizenv.app.payload.WardDto;
import com.citizenv.app.service.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:8080/")
@RestController
@RequestMapping("api/v1/ward")
public class WardController {
    @Autowired
    WardService wardService;

    @GetMapping("/")
    public ResponseEntity<List<WardDto>> getAll() {
        List<WardDto> wardDtoList = wardService.getAll();
        return new ResponseEntity<List<WardDto>>(wardDtoList, HttpStatus.OK);
    }

    @GetMapping("/{wardCode}")
    public ResponseEntity<WardDto> getById(@PathVariable String wardCode) {
        WardDto wardDto = wardService.getById(wardCode);
        return new ResponseEntity<>(wardDto, HttpStatus.OK);
    }

    @PostMapping("/create/{districtCode}/{wardCode}/")
    public ResponseEntity<WardDto> createWard(@PathVariable String districtCode,
                                              @PathVariable String wardCode,
                                              @RequestBody WardDto ward) {
        WardDto wardDto = wardService.createWard(wardCode, districtCode, ward);
        return new ResponseEntity<>(wardDto, HttpStatus.CREATED);
    }

    @PutMapping("/update/{wardCode}/")
    public ResponseEntity<WardDto> updateWard(@PathVariable String wardCode,
                                              @RequestBody WardDto ward) {
        WardDto wardDto = wardService.updateWard(wardCode, ward);
        return new ResponseEntity<>(wardDto, HttpStatus.OK);
    }
}