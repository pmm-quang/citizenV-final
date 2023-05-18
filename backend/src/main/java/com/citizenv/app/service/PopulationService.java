package com.citizenv.app.service;

import com.citizenv.app.payload.population.DivisionGeneralPopulationDto;
import com.citizenv.app.payload.population.PopulationDto;

import java.util.List;

public interface PopulationService {
    Long getCountryPopulation();
    List<DivisionGeneralPopulationDto> getProvincePopulations();
    List<DivisionGeneralPopulationDto> getDistrictPopulationsByProvince(String provinceCode);
    List<DivisionGeneralPopulationDto> getWardPopulationsByDistrict(String districtCode);
    List<DivisionGeneralPopulationDto> getHamletPopulationsByWard(String wardCode);

    List<PopulationDto> getPopulationsBySex();

    List<PopulationDto> getPopulationsByAgeGroup();

    List<PopulationDto> getRegionPopulations();

    List<PopulationDto> getPopulationsByCitizenProperty(String citizenProperty);

    List<DivisionGeneralPopulationDto> getProvincePopulationsByCitizenProperty(String property);
}
