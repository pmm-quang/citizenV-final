package com.citizenv.app.service.impl;

import com.citizenv.app.entity.Address;
import com.citizenv.app.entity.custom.Population;
import com.citizenv.app.repository.AddressRepository;
import com.citizenv.app.repository.CitizenRepository;
import com.citizenv.app.service.PopulationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PopulationServiceImpl implements PopulationService {

    final
    CitizenRepository citizenRepository;

    final
    AddressRepository addressRepository;

    public PopulationServiceImpl(CitizenRepository citizenRepository, AddressRepository addressRepository) {
        this.citizenRepository = citizenRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public Long getCountryPopulation() {
        return citizenRepository.count();
    }

    @Override
    public List<Population> getProvincePopulations() {
        List<Address> addressesForPopulationCount = addressRepository.findByAddressType_Id(2);
        List<Population> result = new ArrayList<>();
        for (Address current :
                addressesForPopulationCount) {
            String currentName = current.getHamlet().getWard().getDistrict().getProvince().getName();
            result.stream().filter(population -> population.getName().equals(currentName)).findFirst().ifPresentOrElse(currentPopulation -> currentPopulation.increasePopulation(1L), () -> result.add(new Population(currentName, 1L)));
        }
        return result;
    }

    @Override
    public List<Population> getDistrictPopulationsByProvince(String provinceCode) {
        List<Address> addressesForPopulationCount = addressRepository.findByAddressType_Id(2);
        List<Population> result = new ArrayList<>();
        for (Address current :
                addressesForPopulationCount) {
            if (current.getHamlet().getWard().getDistrict().getProvince().getCode().equals(provinceCode)) {
                String currentName = current.getHamlet().getWard().getDistrict().getName();
                result.stream().filter(population -> population.getName().equals(currentName)).findFirst().ifPresentOrElse(currentPopulation -> currentPopulation.increasePopulation(1L), () -> result.add(new Population(currentName, 1L)));
            }
        }
        return result;
    }

    @Override
    public List<Population> getWardPopulationsByDistrict(String districtCode) {
        List<Address> addressesForPopulationCount = addressRepository.findByAddressType_Id(2);
        List<Population> result = new ArrayList<>();
        for (Address current :
                addressesForPopulationCount) {
            if (current.getHamlet().getWard().getDistrict().getCode().equals(districtCode)) {
                String currentName = current.getHamlet().getWard().getName();
                result.stream().filter(population -> population.getName().equals(currentName)).findFirst().ifPresentOrElse(currentPopulation -> currentPopulation.increasePopulation(1L), () -> result.add(new Population(currentName, 1L)));
            }
        }
        return result;
    }

    @Override
    public List<Population> getHamletPopulationsByWard(String wardCode) {
        List<Address> addressesForPopulationCount = addressRepository.findByAddressType_Id(2);
        List<Population> result = new ArrayList<>();
        for (Address current :
                addressesForPopulationCount) {
            if (current.getHamlet().getWard().getCode().equals(wardCode)) {
                String currentName = current.getHamlet().getName();
                result.stream().filter(population -> population.getName().equals(currentName)).findFirst().ifPresentOrElse(currentPopulation -> currentPopulation.increasePopulation(1L), () -> result.add(new Population(currentName, 1L)));
            }
        }
        return result;
    }

    @Override
    public List<Population> getPopulationsBySex() {
        List<Address> addressesForPopulationCount = addressRepository.findByAddressType_Id(2);
        List<Population> result = new ArrayList<>();
        for (Address current :
                addressesForPopulationCount) {
            String currentName = current.getCitizen().getSex();
            result.stream().filter(population -> population.getName().equals(currentName)).findFirst().ifPresentOrElse(currentPopulation -> currentPopulation.increasePopulation(1L), () -> result.add(new Population(currentName, 1L)));
        }
        return result;
    }

    @Override
    public List<Population> getPopulationsByAgeGroup() {
        List<Address> addressesForPopulationCount = addressRepository.findByAddressType_Id(2);
        List<Population> result = new ArrayList<>();
        result.add(new Population("Dưới độ tuổi lao động", 0L));
        result.add(new Population("Trong độ tuổi lao động", 0L));
        result.add(new Population("Trên độ tuổi lao động", 0L));
        for (Address current :
                addressesForPopulationCount) {
            Integer currentValue = current.getCitizen().getAge();
            if (currentValue >= 0 && currentValue <= 14) {
                result.get(0).increasePopulation(1L);
            }
            if (currentValue >= 15 && currentValue <= 59) {
                result.get(1).increasePopulation(1L);
            }
            if (currentValue >= 60) {
                result.get(2).increasePopulation(1L);
            }
        }
        return result;
    }
}
