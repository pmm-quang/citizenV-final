package com.citizenv.app.service;

import com.citizenv.app.entity.custom.Population;

import java.util.List;

public interface PopulationService {
    Long getCountryPopulation();
    List<Population> getProvincePopulations();
    List<Population> getDistrictPopulationsByProvince(String provinceCode);
    List<Population> getWardPopulationsByDistrict(String districtCode);
    List<Population> getHamletPopulationsByWard(String wardCode);

    List<Population> getPopulationsBySex();

    List<Population> getPopulationsByAgeGroup();
}
