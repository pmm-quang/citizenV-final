package com.citizenv.app.service;

import com.citizenv.app.entity.Province;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.ProvinceDto;
import com.citizenv.app.repository.AdministrativeRegionRepository;
import com.citizenv.app.repository.AdministrativeUnitRepository;
import com.citizenv.app.repository.ProvinceRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProvinceService {

    Logger logger = LogManager.getRootLogger();
    StringBuilder sb = new StringBuilder();
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private AdministrativeRegionRepository administrativeRegionRepository;
    @Autowired
    private AdministrativeUnitRepository administrativeUnitRepository;
    @Autowired
    private ProvinceRepository repository;

    public List<ProvinceDto> getAll() {
        List<Province> provinceEntities = repository.findAll();
        return provinceEntities.stream().map(l-> mapper.map(l, ProvinceDto.class)).collect(Collectors.toList());
    }

    public ProvinceDto getById(String provinceId) {
        Province province = repository.findById(provinceId).orElseThrow(
                () -> new ResourceNotFoundException("Province", "provinceId", Long.parseLong(provinceId)));
        return mapper.map(province, ProvinceDto.class);
    }

    public ProvinceDto createProvince(Map<String, Object> JSONInfoAsMap) {
        boolean isAllPropertiesFound = JSONInfoAsMap.containsKey("id") &&
                JSONInfoAsMap.containsKey("name") &&
                JSONInfoAsMap.containsKey("administrativeRegionId") &&
                JSONInfoAsMap.containsKey("administrativeUnitId");
        if (!isAllPropertiesFound) {
            return null;
        }
        String id = (String) JSONInfoAsMap.get("code");
        Optional<Province> foundProvince = repository.findById(id);
        if (foundProvince.isEmpty()) {
            logger.trace("Found no such Id. Safe to save");
            Province newProvince = new Province();
            administrativeRegionRepository.findById((Integer) JSONInfoAsMap.get("administrativeRegionId")).ifPresent(foundAdmRegion -> {
                administrativeUnitRepository.findById((Integer) JSONInfoAsMap.get("administrativeUnitId")).ifPresent(foundAdmUnit -> {
                    newProvince.setCode(id);
                    newProvince.setName((String) JSONInfoAsMap.get("name"));
                    newProvince.setAdministrativeRegion(foundAdmRegion);
                    newProvince.setAdministrativeUnit(foundAdmUnit);
                    logger.trace("set things done");
                });
            });
            repository.save(newProvince);
            logger.trace("provinceCreated");
            return mapper.map(newProvince, ProvinceDto.class);
        }
        return null;
    }

    public ProvinceDto updateProvince(String provinceIdNeedUpdate, Map<String, Object> JSONInfoAsMap) {
        Optional<Province> foundProvince = repository.findById(provinceIdNeedUpdate);
        if (foundProvince.isPresent()) {
            Province provinceNeedChange = foundProvince.get();
            logger.trace("Found the thing. Updating...");
            for (String property :
                    JSONInfoAsMap.keySet()) {
                switch (property) {
                    case "code": continue;
                    case "name": {
                        provinceNeedChange.setName((String) JSONInfoAsMap.get(property));
                        logger.trace("Name changed");
                        break;
                    }
                    case "administrativeUnitId": {
                        administrativeUnitRepository.findById((Integer) JSONInfoAsMap.get("administrativeUnitId")).ifPresent(provinceNeedChange::setAdministrativeUnit);
                        logger.trace("admUnit changed");
                        break;
                    }
                    case "administrativeRegionId": {
                        administrativeRegionRepository.findById((Integer) JSONInfoAsMap.get("administrativeRegionId")).ifPresent(provinceNeedChange::setAdministrativeRegion);
                        logger.trace("admRegion changed");
                        break;
                    }
                }

            }
            repository.save(provinceNeedChange);
            logger.trace("provinceUpdated");
            return mapper.map(provinceNeedChange, ProvinceDto.class);
        }
        return null;
    }

    public String deleteById(String provinceId) {
        repository.delete(repository.findById(provinceId).orElseThrow(() -> new ResourceNotFoundException("Province", "provinceId", Long.parseLong(provinceId))));
        return "Deleted";
    }
}
