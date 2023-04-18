package com.citizenv.app.service.impl;


import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.Citizen;
import com.citizenv.app.entity.custom.Population;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.CitizenDto;
import com.citizenv.app.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CitizenServiceImpl {
    @Autowired
    private ModelMapper mapper;

    Logger logger = LogManager.getRootLogger();

    @Autowired
    private CitizenRepository repository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private WardRepository wardRepository;


    public List<CitizenDto> getAll() {
        List<Citizen> entities = repository.findAll();
        return entities.stream().map(l-> mapper.map(l, CitizenDto.class)).collect(Collectors.toList());
    }

    public CitizenDto getById(String citizenId) {
        Citizen citizen = repository.findById(citizenId).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "CustomerID", citizenId));
        return mapper.map(citizen, CitizenDto.class);
    }

    public CitizenDto createCitizen(Map<String, Object> JSONInfoAsMap) {

        /*boolean isAllPropertiesFound = JSONInfoAsMap.containsKey("id") &&
                JSONInfoAsMap.containsKey("name") &&
                JSONInfoAsMap.containsKey("sex") &&
                JSONInfoAsMap.containsKey("dob") &&
                JSONInfoAsMap.containsKey("provinceCode") &&
                JSONInfoAsMap.containsKey("districtCode") &&
                JSONInfoAsMap.containsKey("wardCode");
        if (!isAllPropertiesFound) {
            return null;
        }

        //Validate name
        if (!Utils.validateName((String) JSONInfoAsMap.get("name"))) {
            return null;
        }
        if (!Utils.validateSex((String) JSONInfoAsMap.get("sex"))) {
            return null;
        }

        String id = (String) JSONInfoAsMap.get("id");
        if (!Utils.validateNationalId((String) JSONInfoAsMap.get("id"))) {
            return null;
        }

        Optional<Citizen> foundCitizen = repository.findById(id);
        if (foundCitizen.isEmpty()) {
            logger.trace("Found no such Id. Safe to save");
            Citizen newCitizen = new Citizen();
            provinceRepository.findById((String) JSONInfoAsMap.get("provinceCode")).ifPresent(foundProvince -> {
                districtRepository.findById((String) JSONInfoAsMap.get("districtCode")).ifPresent(foundDistrict -> {
                    wardRepository.findById((String) JSONInfoAsMap.get("wardCode")).ifPresent(foundWard -> {
                        newCitizen.setId(id);
                        newCitizen.setName((String) JSONInfoAsMap.get("name"));
                        newCitizen.setSex((String) JSONInfoAsMap.get("sex"));
                        newCitizen.setDateOfBirth(LocalDate.parse((String) JSONInfoAsMap.get("dob"), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        newCitizen.setProvince(foundProvince);
                        newCitizen.setDistrict(foundDistrict);
                        newCitizen.setWard(foundWard);
                        logger.trace("set things done");
                    });
                });
            });
            repository.save(newCitizen);
            logger.trace("citizenCreated");
            return mapper.map(newCitizen, CitizenDto.class);
        }*/
        return null;
    }

    public CitizenDto updateCitizen(String citizenIdNeedUpdate, Map<String, Object> JSONInfoAsMap) throws ParseException {
        Optional<Citizen> foundCitizen = repository.findById(citizenIdNeedUpdate);
        if (foundCitizen.isPresent()) {
            Citizen citizenNeedChange = foundCitizen.get();
            logger.trace("Found the thing. Updating...");
            for (String property :
                    JSONInfoAsMap.keySet()) {
                switch (property) {
                    case "id": {
                        if (!Utils.validateNationalId(property)) {
                            return null;
                        }
                        citizenNeedChange.setId((String) JSONInfoAsMap.get(property));
                        break;
                    }
                    case "name": {
                        if (!Utils.validateName(property)) {
                            return null;
                        }
                        citizenNeedChange.setName((String) JSONInfoAsMap.get(property));
                        logger.trace("Name changed");
                        break;
                    }
                    case "sex": {
                        if (!Utils.validateSex(property)) {
                            return null;
                        }
                        citizenNeedChange.setSex((String) JSONInfoAsMap.get(property));
                        logger.trace("Sex changed");
                        break;
                    }
                    case "dob": {
                        citizenNeedChange.setDateOfBirth(LocalDate.parse((String) JSONInfoAsMap.get(property), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        logger.trace("Date changed");
                        break;
                    }
                }

            }
            repository.save(citizenNeedChange);
            logger.trace("citizenUpdated");
            return mapper.map(citizenNeedChange, CitizenDto.class);
        }
        return null;
    }

    public String deleteById(String citizenId) {

        repository.delete(repository.findById(citizenId)
                .orElseThrow(() -> new ResourceNotFoundException("Citizen", "citizenId", citizenId)));
        return "Deleted";
    }

    public Long getCountryPopulation() {
        return repository.count();
    }

    public List<Population> getPopulationListGroupByProvince() {
//        return repository.getPopulationGroupByProvince();
        return null;
    }

    public List<Population> getPopulationListGroupByDistrict() {
//        return repository.getPopulationGroupByDistrict();
        return null;
    }

    public List<Population> getPopulationListGroupByWard() {
//        return repository.getPopulationGroupByWard();
        return null;
    }
}
