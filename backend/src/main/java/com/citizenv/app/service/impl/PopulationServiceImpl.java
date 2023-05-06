package com.citizenv.app.service.impl;

import com.citizenv.app.entity.custom.Population;
import com.citizenv.app.repository.AddressRepository;
import com.citizenv.app.repository.CitizenRepository;
import com.citizenv.app.service.PopulationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PopulationServiceImpl implements PopulationService {
    CitizenRepository citizenRepository;
    AddressRepository addressRepository;
    @Override
    public Long getAllPopulation() {
        return citizenRepository.count();
    }

    @Override
    public List<Population> getProvincePopulations() {
        /*return addressRepository.countByAddressTypeId(2);*/
        return null;
    }

    @Override
    public List<Population> getDistrictPopulationsByProvince(String provinceCode) {
        return null;
    }

    @Override
    public List<Population> getWardPopulationsByDistrict(String districtCode) {
        return null;
    }

    @Override
    public List<Population> getHamletPopulationsByWard(String wardCode) {
        return null;
    }
}
