package com.citizenv.app.controller;

import com.citizenv.app.payload.DistrictDto;

import com.citizenv.app.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:3001/"})
@RestController
@RequestMapping("api/v1/district")
public class DistrictController {
    @Autowired
    private DistrictService districtService;

    @GetMapping("/")//OK
    public ResponseEntity<List<DistrictDto>> getAll() {
        List<DistrictDto> districtDtoList = districtService.getAll();
        return new ResponseEntity<List<DistrictDto>>(districtDtoList, HttpStatus.OK);
    }

    @GetMapping("/{districtCode}")//OK
    public ResponseEntity<DistrictDto> getByCode(@PathVariable String districtCode) {
        DistrictDto districtDto = districtService.getByCode(districtCode);
        return new ResponseEntity<>(districtDto, HttpStatus.OK);
    }

    @GetMapping("/provinceCode/{provinceCode}")
    public ResponseEntity<Object> getAllByProvinceCode(@PathVariable String provinceCode) {
        try {
            List<DistrictDto> list = districtService.getAllByProvinceCode(provinceCode);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<DistrictDto> createDistrict(@RequestBody DistrictDto district) {
        DistrictDto districtDto = districtService.createDistrict(district);
        return new ResponseEntity<>(districtDto, HttpStatus.CREATED);
    }

    @PutMapping("/save/{districtCode}")
    public ResponseEntity<DistrictDto> updateDistrict(@PathVariable String districtCode,
                                                      @RequestBody DistrictDto district) {
        DistrictDto districtDto = districtService.updateDistrict(districtCode, district);
        return ResponseEntity.ok().body(district);
    }
}