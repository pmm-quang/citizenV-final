package com.citizenv.app.controller;

import com.citizenv.app.payload.DistrictDto;
import com.citizenv.app.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8080/")
@RestController
@RequestMapping("api/v1/district")
public class DistrictController {
    @Autowired
    DistrictService districtService;

    @GetMapping("/")
    public ResponseEntity<List<DistrictDto>> getAll() {
        List<DistrictDto> districtDtoList = districtService.getAll();
        return new ResponseEntity<List<DistrictDto>>(districtDtoList, HttpStatus.OK);
    }

    @GetMapping("/{districtId}")
    public ResponseEntity<DistrictDto> getById(@PathVariable String districtId) {
        DistrictDto districtDto = districtService.getById(districtId);
        return new ResponseEntity<>(districtDto, HttpStatus.OK);
    }
}