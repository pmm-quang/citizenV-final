package com.citizenv.app.controller;

import com.citizenv.app.payload.population.*;
import com.citizenv.app.payload.request.DivisionPopulationRequest;
import com.citizenv.app.service.StatisticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:3001/"})
@RestController
@RequestMapping("api/v1/statistics")
public class StatisticsController {

    private final StatisticsService populationService;

    public StatisticsController(StatisticsService populationService) {
        this.populationService = populationService;
    }

    @GetMapping("/population")
    public ResponseEntity<Long> getCountryPopulation() {
        Long population = populationService.getCountryPopulation();
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping("/population/ex")
    public ResponseEntity<List<PopulationDto>> getPopulation(@RequestBody Map<String, Object> body) {
        List<PopulationDto> population = populationService.getPopulation(body);
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping("/population/region")
    public ResponseEntity<List<PopulationDto>> getRegionPopulationList() {
        List<PopulationDto> population = populationService.getRegionPopulationList();
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping("/population/urban-rural")
    public ResponseEntity<List<PopulationDto>> getUrbanAndRuralAreaPopulation() {
        List<PopulationDto> population = populationService.getUrbanAndRuralAreaPopulation();
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @PostMapping("/population/division")
    public ResponseEntity<List<DivisionGeneralPopulationDto>> getDivisionPopulationList(@RequestBody DivisionPopulationRequest request) {
        System.out.println(request.toString());
        List<DivisionGeneralPopulationDto> population = populationService.getDivisionPopulationList(request);
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping("/population/district/{provinceCode}")
    public ResponseEntity<List<DivisionGeneralPopulationDto>> getDistrictPopulationListByProvince(@PathVariable String provinceCode) {
        List<DivisionGeneralPopulationDto> population = populationService.getDistrictPopulationListByProvince(provinceCode);
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping("/population/ward/{districtCode}")
    public ResponseEntity<List<DivisionGeneralPopulationDto>> getWardPopulationListByDistrict(@PathVariable String districtCode) {
        List<DivisionGeneralPopulationDto> population = populationService.getWardPopulationListByDistrict(districtCode);
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping("/population/hamlet/{wardCode}")
    public ResponseEntity<List<DivisionGeneralPopulationDto>> getHamletPopulationListByWard(@PathVariable String wardCode) {
        List<DivisionGeneralPopulationDto> population = populationService.getHamletPopulationListByWard(wardCode);
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping("/population/citizen/age-group")
    public ResponseEntity<AgeGroupDto> getPopulationListByAgeGroup() {
        AgeGroupDto population = populationService.getPopulationListByAgeGroup(LocalDate.now().getYear());
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping(value = "/population/citizen", params = "property")
    public ResponseEntity<List<PopulationDto>> getPopulationListByCitizenProperty(@RequestParam String property) {
        List<PopulationDto> population = populationService.getPopulationListByCitizenProperty(property);
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    /*@GetMapping(value = "/population/province/citizen", params = "property")
    public ResponseEntity<List<DivisionPopulationByCitizenPropertyDto>> getProvincePopulationListByCitizenProperty(@RequestParam String property) {
        List<DivisionPopulationByCitizenPropertyDto> population = populationService.getProvincePopulationListByCitizenProperty(property);
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping(value = "/population/district/citizen", params = "property")
    public ResponseEntity<List<DivisionPopulationByCitizenPropertyDto>> getDistrictPopulationListByCitizenProperty(@RequestParam String property) {
        List<DivisionPopulationByCitizenPropertyDto> population = populationService.getProvincePopulationListByCitizenProperty(property);
        return new ResponseEntity<>(population, HttpStatus.OK);
    }*/

    @GetMapping(value = "/population/citizen/age-group", params = {"startYear", "endYear"})
    public ResponseEntity<List<AgeGroupDto>> getPopulationListByAgeGroup(@RequestParam Integer startYear, @RequestParam Integer endYear) {
        List<AgeGroupDto> population = populationService.getPopulationListByAgeGroup(startYear, endYear);
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping(value = "/population/citizen/age-group", params = "year")
    public ResponseEntity<AgeGroupDto> getPopulationListByAgeGroup(Integer year) {
        AgeGroupDto population = populationService.getPopulationListByAgeGroup(year);
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping("/avg-age/province")
    public ResponseEntity<List<AverageAgeDto>> getProvinceAverageAgeList() {
        List<AverageAgeDto> population = populationService.getProvinceAverageAgeList();
        return new ResponseEntity<>(population, HttpStatus.OK);
    }
    @GetMapping("/test")
    public ResponseEntity<List<Map<String, Object>>> test() {
        List<Map<String, Object>> population = populationService.test();
        return new ResponseEntity<>(population, HttpStatus.OK);
    }
}
