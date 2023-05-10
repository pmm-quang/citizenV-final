package com.citizenv.app.controller;

import com.citizenv.app.entity.custom.Population;
import com.citizenv.app.service.PopulationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("api/v1/statistics")
public class StatisticsController {

    private final PopulationService populationService;

    public StatisticsController(PopulationService populationService) {
        this.populationService = populationService;
    }

    @GetMapping("/population")
    public ResponseEntity<Long> getCountryPopulation() {
        Long population = populationService.getCountryPopulation();
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping("/population/province")
    public ResponseEntity<List<Population>> getProvincePopulations() {
        List<Population> population = populationService.getProvincePopulations();
        return new ResponseEntity<List<Population>>(population, HttpStatus.OK);
    }

    @GetMapping("/population/district/{provinceCode}")
    public ResponseEntity<List<Population>> getDistrictPopulationsByProvince(@PathVariable String provinceCode) {
        List<Population> population = populationService.getDistrictPopulationsByProvince(provinceCode);
        return new ResponseEntity<List<Population>>(population, HttpStatus.OK);
    }

    @GetMapping("/population/ward/{districtCode}")
    public ResponseEntity<List<Population>> getWardPopulationsByDistrict(@PathVariable String districtCode) {
        List<Population> population = populationService.getWardPopulationsByDistrict(districtCode);
        return new ResponseEntity<List<Population>>(population, HttpStatus.OK);
    }

    @GetMapping("/population/hamlet/{wardCode}")
    public ResponseEntity<List<Population>> getHamletPopulationsByWard(@PathVariable String wardCode) {
        List<Population> population = populationService.getHamletPopulationsByWard(wardCode);
        return new ResponseEntity<List<Population>>(population, HttpStatus.OK);
    }

    @GetMapping("/population/citizen/sex")
    public ResponseEntity<List<Population>> getPopulationsBySex() {
        List<Population> population = populationService.getPopulationsBySex();
        return new ResponseEntity<List<Population>>(population, HttpStatus.OK);
    }

    @GetMapping("/population/citizen/age-group")
    public ResponseEntity<List<Population>> getPopulationsByAgeGroup() {
        List<Population> population = populationService.getPopulationsByAgeGroup();
        return new ResponseEntity<List<Population>>(population, HttpStatus.OK);
    }
}
