package com.citizenv.app.controller;

import com.citizenv.app.payload.CitizenDto;
import com.citizenv.app.payload.custom.CustomCitizenRequest;
import com.citizenv.app.service.impl.CitizenServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:3001/"})
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

    @GetMapping(value = "/", params = "page")
    public ResponseEntity<Map<String, Object>> getAll(@RequestParam int page) {
        Map<String, Object> dtoPaginationList = citizenService.getAll(page);
        return new ResponseEntity<>(dtoPaginationList, HttpStatus.OK);
    }

    @GetMapping("/{nationalId}")
    public ResponseEntity<CitizenDto> getByNationalId(@PathVariable String nationalId) {
        CitizenDto citizenDto = citizenService.getByNationalId(nationalId);
        return new ResponseEntity<>(citizenDto, HttpStatus.OK);
    }

    @GetMapping(value = "/hamlet/{hamletCode}", params = "page")
    public ResponseEntity<Map<String, Object>> getAllByHamletCode(@PathVariable String hamletCode, @RequestParam int page) {
        Map<String, Object> list = citizenService.getAllByHamletCode(hamletCode, page);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/ward/{wardCode}", params = "page")
    public ResponseEntity<Map<String, Object>> getAllByWardCode(@PathVariable String wardCode, @RequestParam int page) {
        Map<String, Object> list = citizenService.getAllByWardCode(wardCode, page);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/district/{districtCode}", params = "page")
    public ResponseEntity<Map<String, Object>> getAllByDistrictCode(@PathVariable String districtCode, @RequestParam int page) {
        Map<String, Object> list = citizenService.getAllByDistrictCode(districtCode, page);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/province/{provinceCode}", params = "page")
    public ResponseEntity<Map<String, Object>> getAllByProvinceCode(@PathVariable String provinceCode, @RequestParam int page) {
        Map<String, Object> list = citizenService.getAllByProvinceCode(provinceCode, page);
        return new ResponseEntity<>(list, HttpStatus.OK);
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
    public ResponseEntity<CitizenDto> createCitizen(@RequestBody CustomCitizenRequest citizen) {
        CitizenDto citizenDto = citizenService.createCitizen(citizen);
        return new ResponseEntity<>(citizenDto, HttpStatus.CREATED);
    }

    @PutMapping("/save/{citizenId}")
    public ResponseEntity<CitizenDto> updateCitizen(@PathVariable String citizenId, @RequestBody CustomCitizenRequest citizen) {
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
