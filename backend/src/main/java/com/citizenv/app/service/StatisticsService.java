package com.citizenv.app.service;

import com.citizenv.app.payload.population.*;
import com.citizenv.app.payload.request.DivisionPopulationRequest;

import java.util.List;
import java.util.Map;

public interface StatisticsService {
    Long getCountryPopulation();
    List<DivisionGeneralPopulationDto> getDivisionPopulationList(DivisionPopulationRequest request);
    List<DivisionGeneralPopulationDto> getDistrictPopulationListByProvince(String provinceCode);
    List<DivisionGeneralPopulationDto> getWardPopulationListByDistrict(String districtCode);
    List<DivisionGeneralPopulationDto> getHamletPopulationListByWard(String wardCode);

    AgeGroupDto getPopulationListByAgeGroup(Integer year);

    List<PopulationDto> getRegionPopulationList();

    List<PopulationDto> getPopulationListByCitizenProperty(String citizenProperty);

    List<AverageAgeDto> getProvinceAverageAgeList();

    List<AverageAgeByYearDto> getAverageAgeByDivisionCodeAndYearRange(String divisionCode, int startYear, int endYear);

    List<AgeGroupDto> getPopulationListByAgeGroup(Integer startYear, Integer endYear);

    List<PopulationDto> getUrbanAndRuralAreaPopulation();

    List<Map<String, Object>> test();
}
