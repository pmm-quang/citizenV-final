package com.citizenv.app.service;

import com.citizenv.app.entity.custom.Population;

import java.util.List;

public interface PopulationService {
    Long getAllPopulation();
    List<Population> getProvincePopulations();
    List<Population> getDistrictPopulationsByProvince(String provinceCode);
    List<Population> getWardPopulationsByDistrict(String districtCode);
    List<Population> getHamletPopulationsByWard(String wardCode);
}
