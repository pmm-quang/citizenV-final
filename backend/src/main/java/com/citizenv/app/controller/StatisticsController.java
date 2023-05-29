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

@RestController
@RequestMapping("api/v1/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService populationService) {
        this.statisticsService = populationService;
    }

    @GetMapping("/population")
    public ResponseEntity<Long> getCountryPopulation() {
        Long population = statisticsService.getCountryPopulation();
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping("/population/region")
    public ResponseEntity<List<PopulationDto>> getRegionPopulationList() {
        List<PopulationDto> population = statisticsService.getRegionPopulationList();
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping("/population/urban-rural")
    public ResponseEntity<List<PopulationDto>> getUrbanAndRuralAreaPopulation() {
        List<PopulationDto> population = statisticsService.getUrbanAndRuralAreaPopulation();
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @PostMapping("/population/division")
    public ResponseEntity<List<DivisionGeneralPopulationDto>> getDivisionPopulationList(@RequestBody DivisionPopulationRequest request) {
        List<DivisionGeneralPopulationDto> population = statisticsService.getDivisionPopulationList(request);
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping("/population/district/{provinceCode}")
    public ResponseEntity<List<DivisionGeneralPopulationDto>> getDistrictPopulationListByProvince(@PathVariable String provinceCode) {
        List<DivisionGeneralPopulationDto> population = statisticsService.getDistrictPopulationListByProvince(provinceCode);
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping("/population/ward/{districtCode}")
    public ResponseEntity<List<DivisionGeneralPopulationDto>> getWardPopulationListByDistrict(@PathVariable String districtCode) {
        List<DivisionGeneralPopulationDto> population = statisticsService.getWardPopulationListByDistrict(districtCode);
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping("/population/hamlet/{wardCode}")
    public ResponseEntity<List<DivisionGeneralPopulationDto>> getHamletPopulationListByWard(@PathVariable String wardCode) {
        List<DivisionGeneralPopulationDto> population = statisticsService.getHamletPopulationListByWard(wardCode);
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping("/population/citizen/age-group")
    public ResponseEntity<AgeGroupDto> getPopulationListByAgeGroup() {
        AgeGroupDto population = statisticsService.getPopulationListByAgeGroup(LocalDate.now().getYear());
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping(value = "/population/citizen", params = "property")
    public ResponseEntity<List<PopulationDto>> getPopulationListByCitizenProperty(@RequestParam String property) {
        List<PopulationDto> population = statisticsService.getPopulationListByCitizenProperty(property);
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping(value = "/population/citizen/age-group", params = {"startYear", "endYear"})
    public ResponseEntity<List<AgeGroupDto>> getPopulationListByAgeGroup(@RequestParam Integer startYear, @RequestParam Integer endYear) {
        List<AgeGroupDto> population = statisticsService.getPopulationListByAgeGroup(startYear, endYear);
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping(value = "/population/citizen/age-group/{divisionCode}", params = {"startYear", "endYear"})
    public ResponseEntity<Map<String, Object>> getPopulationListByAgeGroup(@PathVariable String divisionCode, @RequestParam Integer startYear, @RequestParam Integer endYear) {
        Map<String, Object> population = statisticsService.getPopulationListByAgeGroup(divisionCode, startYear, endYear);
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping(value = "/population/citizen/age-group", params = "year")
    public ResponseEntity<AgeGroupDto> getPopulationListByAgeGroup(Integer year) {
        AgeGroupDto population = statisticsService.getPopulationListByAgeGroup(year);
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping("/avg-age/province")
    public ResponseEntity<List<AverageAgeDto>> getProvinceAverageAgeList() {
        List<AverageAgeDto> population = statisticsService.getProvinceAverageAgeList();
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    @GetMapping(value = "/avg-age", params = {"divisionCode", "startYear", "endYear"})
    public ResponseEntity<Map<String, Object>> getProvinceAverageAgeByDivisionCode(@RequestParam String divisionCode, @RequestParam int startYear, @RequestParam int endYear) {
        Map<String, Object> divisionAvgAgeDtoByYearRange = statisticsService.getAverageAgeByDivisionCodeAndYearRange(divisionCode, startYear, endYear);
        return new ResponseEntity<>(divisionAvgAgeDtoByYearRange, HttpStatus.OK);
    }
}
