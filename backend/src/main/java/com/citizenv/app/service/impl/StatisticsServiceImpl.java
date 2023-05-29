package com.citizenv.app.service.impl;

import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.*;
import com.citizenv.app.exception.InvalidArgumentException;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.population.*;
import com.citizenv.app.payload.request.DivisionPopulationRequest;
import com.citizenv.app.repository.AddressRepository;
import com.citizenv.app.repository.AdministrativeDivisionRepository;
import com.citizenv.app.repository.CitizenRepository;
import com.citizenv.app.service.StatisticsService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    final
    CitizenRepository citizenRepository;

    final
    AddressRepository addressRepository;

    StringBuilder sb = new StringBuilder();

    private final AdministrativeDivisionRepository administrativeDivisionRepository;

    public StatisticsServiceImpl(CitizenRepository citizenRepository, AddressRepository addressRepository,
                                 AdministrativeDivisionRepository administrativeDivisionRepository) {
        this.citizenRepository = citizenRepository;
        this.addressRepository = addressRepository;
        this.administrativeDivisionRepository = administrativeDivisionRepository;

    }

    @Cacheable("addressesForPopulationCount")
    public List<Address> getAddressesForPopulationCount() {
        return addressRepository.findByAddressType_Id(2);
    }

    @Cacheable("addressesForPopulationCount")
    public List<Address> getAddressesForPopulationCount(String code) {
        return addressRepository.findByAddressType_IdAndDivisionCode(2, code);
    }

    @Override
    public Long getCountryPopulation() {
        return citizenRepository.count();
    }

    @Override
    public List<DivisionGeneralPopulationDto> getDivisionPopulationList(DivisionPopulationRequest request) {
        Map<String, Integer> divisionCodeLength = new HashMap<>();
        divisionCodeLength.put("province", 2);
        divisionCodeLength.put("district", 4);
        divisionCodeLength.put("ward", 6);
        divisionCodeLength.put("hamlet", 8);
        List<Address> addressesForPopulationCount;
        List<DivisionGeneralPopulationDto> result = new ArrayList<>();
        if (request.getCodes() == null) {
            addressesForPopulationCount = getAddressesForPopulationCount();
            for (Address currentAddress :
                    addressesForPopulationCount) {
                AdministrativeDivision currentHamlet = currentAddress.getHamlet();
                String currentDivisionCode = currentHamlet.getCode().substring(0, divisionCodeLength.get(request.getDivision()));
                result.stream()
                        .filter(population -> currentDivisionCode.equals(population.getCode()))
                        .findFirst()
                        .ifPresentOrElse(currentPopulation -> currentPopulation.increasePopulation(1L),
                                () -> {
                                    AdministrativeDivision foundDivision = administrativeDivisionRepository.findByCode(currentDivisionCode).orElseThrow(() -> new ResourceNotFoundException("Division", "code", currentDivisionCode));
                                    if (request.getProperties() == null) {
                                        result.add(new DivisionGeneralPopulationDto(foundDivision.getCode(), foundDivision.getName(), 1L));
                                    } else {
                                        DivisionPopulationByCitizenPropertyDto currentDivisionPopulation = new DivisionPopulationByCitizenPropertyDto(foundDivision.getCode(), foundDivision.getName(), 1L,  getDivisionPopulationByProperty(foundDivision.getCode(), request.getProperties()));
                                        result.add(currentDivisionPopulation);
                                    }
                                });
            }
        } else {
            // Tạo 1 List result chứa các DTO với code được cho trong request, vd: ["01", "02"]
            for (String requestCode :
                    request.getCodes()) {
                AdministrativeDivision foundDivision = administrativeDivisionRepository.findByCode(requestCode).orElseThrow(() -> new ResourceNotFoundException("Division", "code", requestCode));
                if (request.getProperties() == null) {
                    DivisionGeneralPopulationDto currentResultDto = new DivisionGeneralPopulationDto(requestCode, foundDivision.getName());
                    result.add(currentResultDto);
                    addressesForPopulationCount = getAddressesForPopulationCount(requestCode);
                    for (Address currentAddress :
                            addressesForPopulationCount) {
                        AdministrativeDivision currentHamlet = currentAddress.getHamlet();
                        if (currentHamlet.getCode().indexOf(requestCode) == 0) {
                            currentResultDto.increasePopulation(1L);
                        }
                    }
                } else {
                    DivisionPopulationByCitizenPropertyDto currentDivisionPopulation = new DivisionPopulationByCitizenPropertyDto(foundDivision.getCode(), foundDivision.getName(), getDivisionPopulationByProperty(foundDivision.getCode(), request.getProperties()));
                    result.add(currentDivisionPopulation);
                    currentDivisionPopulation.recalculatePopulation();
                }

            }
        }
        return result;
    }

    @Override
    public List<DivisionGeneralPopulationDto> getDistrictPopulationListByProvince(String provinceCode) {
        List<Address> addressesForPopulationCount = getAddressesForPopulationCount();
        List<DivisionGeneralPopulationDto> result = new ArrayList<>();
        for (Address currentAddress :
                addressesForPopulationCount) {
            if (currentAddress.getHamlet().getWard().getDistrict().getProvince().getCode().equals(provinceCode)) {
                District currentDistrict = currentAddress.getHamlet().getWard().getDistrict();
                String currentDistrictAdmUnitName = currentDistrict.getAdministrativeUnit().getShortName();
                sb = new StringBuilder();
                result.stream().filter(population -> population.getCode().equals(currentDistrict.getCode())).findFirst().ifPresentOrElse(currentPopulation -> currentPopulation.increasePopulation(1L), () -> result.add(new DivisionGeneralPopulationDto(currentDistrict.getCode(), sb.append(currentDistrictAdmUnitName).append(" ").append(currentDistrict.getName()).toString())));
            }
        }
        return result;
    }

    @Override
    public List<DivisionGeneralPopulationDto> getWardPopulationListByDistrict(String districtCode) {
        List<Address> addressesForPopulationCount = getAddressesForPopulationCount();
        List<DivisionGeneralPopulationDto> result = new ArrayList<>();
        for (Address currentAddress :
                addressesForPopulationCount) {
            if (currentAddress.getHamlet().getWard().getDistrict().getCode().equals(districtCode)) {
                Ward currentWard = currentAddress.getHamlet().getWard();
                String currentWardAdmUnitName = currentWard.getAdministrativeUnit().getShortName();
                sb = new StringBuilder();
                result.stream().filter(population -> population.getCode().equals(currentWard.getCode())).findFirst().ifPresentOrElse(currentPopulation -> currentPopulation.increasePopulation(1L), () -> result.add(new DivisionGeneralPopulationDto(currentWard.getCode(), sb.append(currentWardAdmUnitName).append(" ").append(currentWard.getName()).toString())));
            }
        }
        return result;
    }

    @Override
    public List<DivisionGeneralPopulationDto> getHamletPopulationListByWard(String wardCode) {
        List<Address> addressesForPopulationCount = getAddressesForPopulationCount();
        List<DivisionGeneralPopulationDto> result = new ArrayList<>();
        for (Address currentAddress :
                addressesForPopulationCount) {
            if (currentAddress.getHamlet().getWard().getCode().equals(wardCode)) {
                Hamlet currentHamlet = currentAddress.getHamlet();
                String currentHamletAdmUnitName = currentHamlet.getAdministrativeUnit().getShortName();
                sb = new StringBuilder();
                result.stream().filter(population -> population.getCode().equals(currentHamlet.getCode())).findFirst().ifPresentOrElse(currentPopulation -> currentPopulation.increasePopulation(1L), () -> result.add(new DivisionGeneralPopulationDto(currentHamlet.getCode(), sb.append(currentHamletAdmUnitName).append(" ").append(currentHamlet.getName()).toString())));
            }
        }
        return result;
    }

    @Override
    public AgeGroupDto getPopulationListByAgeGroup(Integer year) {
        if (year > LocalDate.now().getYear()) throw new InvalidArgumentException("Invalid year");
        List<Address> addressesForPopulationCount = getAddressesForPopulationCount();
        AgeGroupDto result = new AgeGroupDto(year);
        result.getAgeGroupPopulation().add(new PopulationDto(Utils.AGE_GROUP_UNDER_LEGAL_WORKING_AGE));
        result.getAgeGroupPopulation().add(new PopulationDto(Utils.AGE_GROUP_IN_LEGAL_WORKING_AGE));
        result.getAgeGroupPopulation().add(new PopulationDto(Utils.AGE_GROUP_OVER_LEGAL_WORKING_AGE));
        for (Address currentAddress :
                addressesForPopulationCount) {
            int ageSinceCurrentYear = Period.between(currentAddress.getCitizen().getDateOfBirth(), LocalDate.of(year, 1, 1)).getYears();
            if (ageSinceCurrentYear >= 0 && ageSinceCurrentYear <= 14) {
                result.getAgeGroupPopulation().get(0).increasePopulation(1L);
            }
            if (ageSinceCurrentYear >= 15 && ageSinceCurrentYear <= 59) {
                result.getAgeGroupPopulation().get(1).increasePopulation(1L);
            }
            if (ageSinceCurrentYear >= 60) {
                result.getAgeGroupPopulation().get(2).increasePopulation(1L);
            }
        }
        return result;
    }

    @Override
    public List<PopulationDto> getRegionPopulationList() {
        List<Address> addressesForPopulationCount = getAddressesForPopulationCount();
        List<PopulationDto> result = new ArrayList<>();
        for (Address currentAddress :
                addressesForPopulationCount) {
            String currentName = currentAddress.getHamlet().getWard().getDistrict().getProvince().getAdministrativeRegion().getName();
            result.stream().filter(population -> population.getName().equals(currentName)).findFirst().ifPresentOrElse(currentPopulation -> currentPopulation.increasePopulation(1L), () -> result.add(new PopulationDto(currentName, 1L)));
        }
        return result;
    }

    @Override
    public List<PopulationDto> getPopulationListByCitizenProperty(String property) {
        List<Address> addressesForPopulationCount = getAddressesForPopulationCount();
        try {
            sb.setLength(0);
            Method method;
            method = Citizen.class.getDeclaredMethod(sb.append("get").append(property.substring(0, 1).toUpperCase()).append(property.substring(1)).toString());
            List<PopulationDto> result = new ArrayList<>();
            for (Address currentAddress :
                    addressesForPopulationCount) {
                Citizen currentCitizen = currentAddress.getCitizen();
                Object currentName = method.invoke(currentCitizen);
                result.stream().filter(population -> population.getName().equals(currentName.toString())).findFirst().ifPresentOrElse(currentPopulation -> currentPopulation.increasePopulation(1L), () -> result.add(new PopulationDto(currentName.toString(), 1L)));
            }
            return result;
        } catch (SecurityException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private List<Map<String, Object>> getDivisionPopulationByProperty(String code, Set<String> properties) {
        try {
            return addressRepository.countByProperties(properties, code);
        } catch (SecurityException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public List<AverageAgeDto> getProvinceAverageAgeList() {
        List<Address> addressesForPopulationCount = getAddressesForPopulationCount();
        List<AverageAgeDto> result = new ArrayList<>();
        Map<String, List<Long>> totalAgeMap = new HashMap<>();
        for (Address currentAddress :
                addressesForPopulationCount) {
            Province currentProvince = currentAddress.getHamlet().getWard().getDistrict().getProvince();
            Integer currentCitizenAge = currentAddress.getCitizen().getAge();
            sb.setLength(0);
            String key = sb.append(currentProvince.getCode()).append(",").append(currentProvince.getName()).toString();
            if (totalAgeMap.containsKey(key)) {
                totalAgeMap.get(key).set(0, totalAgeMap.get(key).get(0) + 1);
                totalAgeMap.get(key).set(1, totalAgeMap.get(key).get(1) + currentCitizenAge);
            } else {
                totalAgeMap.put(key, new ArrayList<>(List.of(1L, currentCitizenAge.longValue())));
            }
        }
        for (Map.Entry<String, List<Long>> entry : totalAgeMap.entrySet()) {
            String[] codeAndNamePair = entry.getKey().split(",");
            result.add(new AverageAgeDto(codeAndNamePair[0], codeAndNamePair[1], entry.getValue().get(1).doubleValue() / entry.getValue().get(0).doubleValue()));
        }
        return result;
    }

    @Override
    public List<AverageAgeByYearDto> getAverageAgeByDivisionCodeAndYearRange(String divisionCode, int startYear, int endYear) {
        List<AverageAgeByYearDto> result = new ArrayList<>();
        for (int currentYear = startYear; currentYear <= endYear; currentYear++) {
            result.add(getAverageAgeByDivisionCodeAndYear(divisionCode, currentYear));
        }
        return result;
    }

    private AverageAgeByYearDto getAverageAgeByDivisionCodeAndYear(String divisionCode, int currentYear) {
        if (currentYear > LocalDate.now().getYear()) throw new InvalidArgumentException("Invalid year");
        List<Address> addressesForPopulationCount = getAddressesForPopulationCount(divisionCode);
        double totalAge = 0;
        long populationCount = 0L;
        for (Address currentAddress :
                addressesForPopulationCount) {
            int ageSinceCurrentYear = Period.between(currentAddress.getCitizen().getDateOfBirth(), LocalDate.of(currentYear, 1, 1)).getYears();
            if (ageSinceCurrentYear > 0) {
                totalAge += ageSinceCurrentYear;
                populationCount++;
            }
            System.out.println("Current year: " + currentYear + ", current address: " + currentAddress.getId() + ", current age: " + ageSinceCurrentYear + ", total: " + totalAge + ", count: " + populationCount);
        }
        AdministrativeDivision currentDivision = administrativeDivisionRepository.findByCode(divisionCode).orElseThrow(() -> new ResourceNotFoundException("Division", "code", divisionCode));
        double averageAge = totalAge / populationCount;
        return new AverageAgeByYearDto(currentDivision.getCode(), currentDivision.getName(), currentYear, averageAge);
    }

    @Override
    public List<AgeGroupDto> getPopulationListByAgeGroup(Integer startYear, Integer endYear) {
        List<AgeGroupDto> result = new ArrayList<>();
        for (Integer i = startYear; i <= endYear; i++) {
            result.add(getPopulationListByAgeGroup(i));
        }
        return result;
    }

    @Override
    public List<PopulationDto> getUrbanAndRuralAreaPopulation() {
        List<Address> addressesForPopulationCount = getAddressesForPopulationCount();
        List<PopulationDto> result = new ArrayList<>();
        result.add(new PopulationDto(Utils.URBAN_AREA));
        result.add(new PopulationDto(Utils.RURAL_AREA));
        for (Address currentAddress :
                addressesForPopulationCount) {
            Integer currentDistrictAdmUnitId = currentAddress.getHamlet().getWard().getDistrict().getAdministrativeUnit().getId();
            result.get(Utils.AdministrativeUnitsLv2.isUrbanArea(currentDistrictAdmUnitId) ? 0 : 1).increasePopulation(1L);
        }
        return result;

    }

    @Override
    public List<Map<String, Object>> test() {
        Set<String> properties = new HashSet<>();
        properties.add("sex");
        properties.add("local");
        String code = "24";
        List<Map<String, Object>> a = addressRepository.countByProperties(properties, code);
        return a;
    }
}
