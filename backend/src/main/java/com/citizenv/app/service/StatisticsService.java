package com.citizenv.app.service;

import com.citizenv.app.payload.population.*;

import java.util.List;

public interface StatisticsService {
    Long getCountryPopulation();
    List<DivisionGeneralPopulationDto> getProvincePopulationList();
    List<DivisionGeneralPopulationDto> getDistrictPopulationListByProvince(String provinceCode);
    List<DivisionGeneralPopulationDto> getWardPopulationListByDistrict(String districtCode);
    List<DivisionGeneralPopulationDto> getHamletPopulationListByWard(String wardCode);

    AgeGroupDto getPopulationListByAgeGroup(Integer year);

    List<PopulationDto> getRegionPopulationList();

    List<PopulationDto> getPopulationListByCitizenProperty(String citizenProperty);

    List<DivisionPopulationByCitizenPropertyDto> getProvincePopulationListByCitizenProperty(String property);

    List<AverageAgeDto> getProvinceAverageAgeList();

    List<AgeGroupDto> getPopulationListByAgeGroup(Integer startYear, Integer endYear);
}
