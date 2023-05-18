package com.citizenv.app.controller;

import com.citizenv.app.payload.population.DivisionGeneralPopulationDto;
import com.citizenv.app.payload.population.PopulationDto;
import com.citizenv.app.service.PopulationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:3001/"})
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

    @GetMapping("/population/region")
    public ResponseEntity<List<PopulationDto>> getRegionPopulations() {
        List<PopulationDto> population = populationService.getRegionPopulations();
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping("/population/province")
    public ResponseEntity<List<DivisionGeneralPopulationDto>> getProvincePopulations() {
        List<DivisionGeneralPopulationDto> population = populationService.getProvincePopulations();
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping("/population/district/{provinceCode}")
    public ResponseEntity<List<DivisionGeneralPopulationDto>> getDistrictPopulationsByProvince(@PathVariable String provinceCode) {
        List<DivisionGeneralPopulationDto> population = populationService.getDistrictPopulationsByProvince(provinceCode);
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping("/population/ward/{districtCode}")
    public ResponseEntity<List<DivisionGeneralPopulationDto>> getWardPopulationsByDistrict(@PathVariable String districtCode) {
        List<DivisionGeneralPopulationDto> population = populationService.getWardPopulationsByDistrict(districtCode);
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping("/population/hamlet/{wardCode}")
    public ResponseEntity<List<DivisionGeneralPopulationDto>> getHamletPopulationsByWard(@PathVariable String wardCode) {
        List<DivisionGeneralPopulationDto> population = populationService.getHamletPopulationsByWard(wardCode);
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping("/population/citizen/sex")
    public ResponseEntity<List<PopulationDto>> getPopulationsBySex() {
        List<PopulationDto> population = populationService.getPopulationsBySex();
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping("/population/citizen/age-group")
    public ResponseEntity<List<PopulationDto>> getPopulationsByAgeGroup() {
        List<PopulationDto> population = populationService.getPopulationsByAgeGroup();
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping(value = "/population/citizen", params = "property")
    public ResponseEntity<List<PopulationDto>> getPopulationsByCitizenProperty(@RequestParam String property) {
        List<PopulationDto> population = populationService.getPopulationsByCitizenProperty(property);
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping(value = "/population/province/citizen", params = "property")
    public ResponseEntity<List<DivisionGeneralPopulationDto>> getProvincePopulationsByCitizenProperty(@RequestParam String property) {
        List<DivisionGeneralPopulationDto> population = populationService.getProvincePopulationsByCitizenProperty(property);
        return new ResponseEntity<>(population, HttpStatus.OK);
    }
}
