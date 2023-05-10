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

    /*Population (dân số)*/
    /*@GetMapping("/population")
    public ResponseEntity<Long> getCountryPopulation() {
        Long population = citizenService.getCountryPopulation();
        return new ResponseEntity<Long>(population, HttpStatus.OK);
    }

    @GetMapping("/population/province")
    public ResponseEntity<List<Population>> getPopulationListGroupByProvince() {
        List<Population> population = citizenService.getPopulationListGroupByProvince();
        return new ResponseEntity<List<Population>>(population, HttpStatus.OK);
    }

    @GetMapping("/population/district")
    public ResponseEntity<List<Population>> getPopulationListGroupByDistrict() {
        List<Population> population = citizenService.getPopulationListGroupByDistrict();
        return new ResponseEntity<List<Population>>(population, HttpStatus.OK);
    }

    @GetMapping("/population/ward")
    public ResponseEntity<List<Population>> getPopulationListGroupByWard() {
        List<Population> population = citizenService.getPopulationListGroupByWard();
        return new ResponseEntity<List<Population>>(population, HttpStatus.OK);
    }*/


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
