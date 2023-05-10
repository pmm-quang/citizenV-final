package com.citizenv.app.controller;

import com.citizenv.app.payload.CitizenDto;
import com.citizenv.app.service.impl.CitizenServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("api/v1/citizen")
public class CitizenController {
    @Autowired
    CitizenServiceImpl citizenService;

    @GetMapping("/")
    public ResponseEntity<List<CitizenDto>> getAll() {
        List<CitizenDto> citizenDtoList = citizenService.getAll();
        return new ResponseEntity<>(citizenDtoList, HttpStatus.OK);
    }

    @GetMapping("/{citizenId}")
    public ResponseEntity<CitizenDto> getById(@PathVariable String citizenId) {
        CitizenDto citizenDto = citizenService.getById(citizenId);
        return new ResponseEntity<>(citizenDto, HttpStatus.OK);
    }

    @GetMapping("/hamlet/{hamletCode}")
    public ResponseEntity<List<CitizenDto>> getAllByHamletCode(@PathVariable String hamletCode) {
        List<CitizenDto> list = citizenService.getAllByHamletCode(hamletCode);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/ward/{wardCode}")
    public ResponseEntity<List<CitizenDto>> getAllByWardCode(@PathVariable String wardCode) {
        List<CitizenDto> list = citizenService.getAllByWardCode(wardCode);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/district/{districtCode}")
    public ResponseEntity<List<CitizenDto>> getAllByDistrictCode(@PathVariable String districtCode) {
        List<CitizenDto> list = citizenService.getAllByDistrictCode(districtCode);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/province/{provinceCode}")
    public ResponseEntity<List<CitizenDto>> getAllByProvinceCode(@PathVariable String provinceCode) {
        List<CitizenDto> list = citizenService.getAllByProvinceCode(provinceCode);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<CitizenDto> createCitizen(@RequestBody CitizenDto citizen) {
        CitizenDto citizenDto = citizenService.createCitizen(citizen);
        return new ResponseEntity<>(citizenDto, HttpStatus.CREATED);
    }

    @PutMapping("/save/{citizenId}")
    public ResponseEntity<CitizenDto> updateCitizen(@PathVariable String citizenId, @RequestBody CitizenDto citizen) {
        CitizenDto citizenDto = citizenService.updateCitizen(citizenId, citizen);
        return new ResponseEntity<>(citizenDto, HttpStatus.OK);
    }

/**
    @PostMapping("/")
    public ResponseEntity<CitizenDto> create(@RequestBody Map<String, Object> citizenJSONInfo) {
        CitizenDto newCitizen = citizenService.createCitizen(citizenJSONInfo);
        return new ResponseEntity<>(newCitizen, HttpStatus.CREATED);
    }

    @PutMapping("/{citizenId}")
    public ResponseEntity<CitizenDto> update(@PathVariable String citizenId, @RequestBody Map<String, Object> citizenJSONInfo) throws NoSuchMethodException, ParseException {
        CitizenDto newCitizen = citizenService.updateCitizen(citizenId, citizenJSONInfo);
        return new ResponseEntity<>(newCitizen, HttpStatus.OK);
    }

    @DeleteMapping("/{citizenId}")
    public ResponseEntity<String> deleteById(@PathVariable String citizenId) {
        return new ResponseEntity<>(citizenService.deleteById(citizenId), HttpStatus.OK);
    }


    **/
}
