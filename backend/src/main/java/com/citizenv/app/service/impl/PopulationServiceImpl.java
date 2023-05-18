package com.citizenv.app.service.impl;

import com.citizenv.app.entity.*;
import com.citizenv.app.payload.population.DivisionGeneralPopulationDto;
import com.citizenv.app.payload.population.PopulationDto;
import com.citizenv.app.repository.AddressRepository;
import com.citizenv.app.repository.CitizenRepository;
import com.citizenv.app.service.PopulationService;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Service
public class PopulationServiceImpl implements PopulationService {

    final
    CitizenRepository citizenRepository;

    final
    AddressRepository addressRepository;

    StringBuilder sb = new StringBuilder();

    public PopulationServiceImpl(CitizenRepository citizenRepository, AddressRepository addressRepository) {
        this.citizenRepository = citizenRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public Long getCountryPopulation() {
        return citizenRepository.count();
    }

    @Override
    public List<DivisionGeneralPopulationDto> getProvincePopulations() {
        List<Address> addressesForPopulationCount = addressRepository.findByAddressType_Id(2);
        List<DivisionGeneralPopulationDto> result = new ArrayList<>();
        for (Address currentAddress :
                addressesForPopulationCount) {
            Province currentProvince = currentAddress.getHamlet().getWard().getDistrict().getProvince();
            result.stream().filter(population -> population.getCode().equals(currentProvince.getCode())).findFirst().ifPresentOrElse(currentPopulation -> currentPopulation.increasePopulation(1L), () -> result.add(new DivisionGeneralPopulationDto(currentProvince.getCode(), currentProvince.getName())));
        }
        return result;
    }

    @Override
    public List<DivisionGeneralPopulationDto> getDistrictPopulationsByProvince(String provinceCode) {
        List<Address> addressesForPopulationCount = addressRepository.findByAddressType_Id(2);
        List<DivisionGeneralPopulationDto> result = new ArrayList<>();
        for (Address currentAddress :
                addressesForPopulationCount) {
            if (currentAddress.getHamlet().getWard().getDistrict().getProvince().getCode().equals(provinceCode)) {
                District currentDistrict = currentAddress.getHamlet().getWard().getDistrict();
                result.stream().filter(population -> population.getCode().equals(currentDistrict.getCode())).findFirst().ifPresentOrElse(currentPopulation -> currentPopulation.increasePopulation(1L), () -> result.add(new DivisionGeneralPopulationDto(currentDistrict.getCode(), currentDistrict.getName())));
            }
        }
        return result;
    }

    @Override
    public List<DivisionGeneralPopulationDto> getWardPopulationsByDistrict(String districtCode) {
        List<Address> addressesForPopulationCount = addressRepository.findByAddressType_Id(2);
        List<DivisionGeneralPopulationDto> result = new ArrayList<>();
        for (Address currentAddress :
                addressesForPopulationCount) {
            if (currentAddress.getHamlet().getWard().getDistrict().getCode().equals(districtCode)) {
                Ward currentWard = currentAddress.getHamlet().getWard();
                result.stream().filter(population -> population.getCode().equals(currentWard.getCode())).findFirst().ifPresentOrElse(currentPopulation -> currentPopulation.increasePopulation(1L), () -> result.add(new DivisionGeneralPopulationDto(currentWard.getCode(), currentWard.getName())));
            }
        }
        return result;
    }

    @Override
    public List<DivisionGeneralPopulationDto> getHamletPopulationsByWard(String wardCode) {
        List<Address> addressesForPopulationCount = addressRepository.findByAddressType_Id(2);
        List<DivisionGeneralPopulationDto> result = new ArrayList<>();
        for (Address currentAddress :
                addressesForPopulationCount) {
            if (currentAddress.getHamlet().getWard().getCode().equals(wardCode)) {
                Hamlet currentHamlet = currentAddress.getHamlet();
                result.stream().filter(population -> population.getCode().equals(currentHamlet.getCode())).findFirst().ifPresentOrElse(currentPopulation -> currentPopulation.increasePopulation(1L), () -> result.add(new DivisionGeneralPopulationDto(currentHamlet.getCode(), currentHamlet.getName())));
            }
        }
        return result;
    }

    @Override
    public List<PopulationDto> getPopulationsBySex() {
        List<Address> addressesForPopulationCount = addressRepository.findByAddressType_Id(2);
        List<PopulationDto> result = new ArrayList<>();
        for (Address currentAddress :
                addressesForPopulationCount) {
            String currentName = currentAddress.getCitizen().getSex();
            result.stream().filter(population -> population.getName().equals(currentName)).findFirst().ifPresentOrElse(currentPopulation -> currentPopulation.increasePopulation(1L), () -> result.add(new PopulationDto(currentName)));
        }
        return result;
    }

    @Override
    public List<PopulationDto> getPopulationsByAgeGroup() {
        List<Address> addressesForPopulationCount = addressRepository.findByAddressType_Id(2);
        List<PopulationDto> result = new ArrayList<>();
        result.add(new PopulationDto("Dưới độ tuổi lao động"));
        result.add(new PopulationDto("Trong độ tuổi lao động"));
        result.add(new PopulationDto("Trên độ tuổi lao động"));
        for (Address currentAddress :
                addressesForPopulationCount) {
            Integer currentValue = currentAddress.getCitizen().getAge();
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

    @Override
    public List<PopulationDto> getRegionPopulations() {
        List<Address> addressesForPopulationCount = addressRepository.findByAddressType_Id(2);
        List<PopulationDto> result = new ArrayList<>();
        for (Address currentAddress :
                addressesForPopulationCount) {
            String currentName = currentAddress.getHamlet().getWard().getDistrict().getProvince().getAdministrativeRegion().getName();
            result.stream().filter(population -> population.getName().equals(currentName)).findFirst().ifPresentOrElse(currentPopulation -> currentPopulation.increasePopulation(1L), () -> result.add(new PopulationDto(currentName)));
        }
        return result;
    }

    @Override
    public List<PopulationDto> getPopulationsByCitizenProperty(String citizenProperty) {
        List<Address> addressesForPopulationCount = addressRepository.findByAddressType_Id(2);
        try {
            sb.setLength(0);
            Method method;
            method = Citizen.class.getDeclaredMethod(sb.append("get").append(citizenProperty.substring(0, 1).toUpperCase()).append(citizenProperty.substring(1)).toString());
            List<PopulationDto> result = new ArrayList<>();
            for (Address currentAddress :
                    addressesForPopulationCount) {
                Citizen currentCitizen = currentAddress.getCitizen();
                Object currentName = method.invoke(currentCitizen);
                result.stream().filter(population -> population.getName().equals(currentName.toString())).findFirst().ifPresentOrElse(currentPopulation -> currentPopulation.increasePopulation(1L), () -> result.add(new PopulationDto(currentName.toString())));
            }
            return result;
        } catch (SecurityException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public List<DivisionGeneralPopulationDto> getProvincePopulationsByCitizenProperty(String property) {
        return null;
    }
}
