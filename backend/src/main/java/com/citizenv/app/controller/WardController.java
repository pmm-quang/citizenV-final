package com.citizenv.app.controller;

import com.citizenv.app.payload.ProvinceDto;
import com.citizenv.app.payload.WardDto;
import com.citizenv.app.payload.custom.CustomHamletRequest;
import com.citizenv.app.payload.custom.CustomWardRequest;
import com.citizenv.app.service.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:3001/"})
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
    public ResponseEntity<WardDto> getByCode(@PathVariable String wardCode) {
        WardDto wardDto = wardService.getByCode(wardCode);
        return new ResponseEntity<>(wardDto, HttpStatus.OK);
    }

    @GetMapping("/districtCode/{districtCode}")
    public ResponseEntity<Object> getAllByDistrictCode(@PathVariable String districtCode) {
        try {
            List<WardDto> wardDtos = wardService.getByDistrictCode(districtCode);
            return new ResponseEntity<>(wardDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/administrativeUnitID/{admUnitID}")
    public ResponseEntity<Object> getAllByAdministrativeUnitID(@PathVariable int admUnitID) {
        try {
            List<WardDto> wardDtos = wardService.getByAdministrativeUnitId(admUnitID);
            return new ResponseEntity<>(wardDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/save")
    public ResponseEntity<Object> createWard(@RequestBody CustomWardRequest ward) {
        WardDto wardDto = wardService.createWard(ward);
       return ResponseEntity.status(201).body(wardDto);
    }

    @PutMapping("/save/{wardCode}/")
    public ResponseEntity<Object> updateWard(@PathVariable String wardCode,
                                              @RequestBody CustomWardRequest ward) {
       WardDto wardDto = wardService.updateWard(wardCode, ward);
       return ResponseEntity.ok().body(wardDto);
    }
}