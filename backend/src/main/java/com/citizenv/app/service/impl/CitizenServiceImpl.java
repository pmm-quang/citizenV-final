package com.citizenv.app.service.impl;


import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.*;
import com.citizenv.app.entity.custom.Population;
import com.citizenv.app.exception.ResourceFoundException;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.AddressDto;
import com.citizenv.app.payload.CitizenDto;
import com.citizenv.app.repository.*;
import com.citizenv.app.service.CitizenService;
import com.mysql.cj.protocol.WriterWatcher;
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
public class CitizenServiceImpl implements CitizenService {
    @Autowired
    private ModelMapper mapper;

    Logger logger = LogManager.getRootLogger();

    @Autowired
    private CitizenRepository repo;
    @Autowired
    private DistrictRepository districtRepo;
    @Autowired
    private ProvinceRepository provinceRepo;
    @Autowired
    private WardRepository wardRepo;
    @Autowired
    private HamletRepository hamletRepo;
    @Autowired
    private AddressRepository addressRepo;


    public List<CitizenDto> getAll() {
        List<Citizen> entities = repo.findAll();
        return entities.stream().map(l-> mapper.map(l, CitizenDto.class)).collect(Collectors.toList());
    }

    public CitizenDto getById(String citizenId) {
        Citizen citizen = repo.findById(citizenId).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "CustomerID", citizenId));
        return mapper.map(citizen, CitizenDto.class);
    }

    @Override
    public List<CitizenDto> getAllByHamletCode(String hamletCode) {
        Hamlet foundHamlet = hamletRepo.findById(hamletCode).orElseThrow(
                ()-> new ResourceNotFoundException("Hamlet", "HamletCode", hamletCode)
        );
//        List<Citizen> entities = repo.findAllByHamletCode(hamletCode, "1");
        List<Citizen> entities = repo.findByAddresses_Hamlet_code(hamletCode);
        return entities.stream().map(l-> mapper.map(l, CitizenDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<CitizenDto> getAllByWardCode(String wardCode) {
        Ward foundWard = wardRepo.findById(wardCode).orElseThrow(
                () -> new ResourceNotFoundException("Ward", "WardCode", wardCode)
        );
        List<Citizen> entities = repo.findAllByWardCode(wardCode, "1");
        return entities.stream().map(l-> mapper.map(l, CitizenDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<CitizenDto> getAllByDistrictCode(String districtCode) {
        District foundDistrict = districtRepo.findById(districtCode).orElseThrow(
                () -> new ResourceNotFoundException("District", "DistrictCode", districtCode)
        );
        List<Citizen> entities = repo.findAllByDistrictCode(districtCode, "1");
        return entities.stream().map(l-> mapper.map(l, CitizenDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<CitizenDto> getAllByProvinceCode(String provinceCode) {
        Province foundProvince = provinceRepo.findById(provinceCode).orElseThrow(
                () -> new ResourceNotFoundException("Province", "ProvinceCode", provinceCode)
        );
        List<Citizen> entities = repo.findAllByProvinceCode(provinceCode, "1");
        return entities.stream().map(l-> mapper.map(l, CitizenDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<CitizenDto> getAllByAddressId(String addressId) {
        return null;
    }

    @Override
    public CitizenDto createCitizen(CitizenDto citizen) {
        String citizenId = citizen.getId();
        repo.findById(citizenId).ifPresent(
                citizen1 -> {throw new ResourceFoundException("Citizen", "CititzenId", citizenId);
        });

        return null;
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
        Optional<Citizen> foundCitizen = repo.findById(citizenIdNeedUpdate);
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
            repo.save(citizenNeedChange);
            logger.trace("citizenUpdated");
            return mapper.map(citizenNeedChange, CitizenDto.class);
        }
        return null;
    }

    public String deleteById(String citizenId) {

        repo.delete(repo.findById(citizenId)
                .orElseThrow(() -> new ResourceNotFoundException("Citizen", "citizenId", citizenId)));
        return "Deleted";
    }

}
