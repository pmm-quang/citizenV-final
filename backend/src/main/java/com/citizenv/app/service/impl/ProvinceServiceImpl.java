package com.citizenv.app.service.impl;

import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.AdministrativeRegion;
import com.citizenv.app.entity.AdministrativeUnit;
import com.citizenv.app.entity.Province;
import com.citizenv.app.exception.ResourceFoundException;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.ProvinceDto;
import com.citizenv.app.repository.AdministrativeRegionRepository;
import com.citizenv.app.repository.AdministrativeUnitRepository;
import com.citizenv.app.repository.ProvinceRepository;
import com.citizenv.app.service.ProvinceService;
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
public class ProvinceServiceImpl implements ProvinceService {

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

    @Override
    public List<ProvinceDto> getAll() {
        List<Province> provinceEntities = repository.findAll();
        return provinceEntities.stream().map(l-> mapper.map(l, ProvinceDto.class)).collect(Collectors.toList());
    }

    @Override
    public ProvinceDto getById(String provinceId) {
        Province province = repository.findById(provinceId).orElseThrow(
                () -> new ResourceNotFoundException("Province", "provinceId", provinceId));
        return mapper.map(province, ProvinceDto.class);
    }

    @Override
    public List<ProvinceDto> getAllByAdministrativeUnitId(int admUnitId) {
        AdministrativeUnit foundAdmUnit = administrativeUnitRepository.findById(admUnitId).orElseThrow(
                () -> new ResourceNotFoundException("AdministrativeUnit", "AdministrativeUnitID",String.valueOf(admUnitId))
        );
        List<Province> list = repository.findAllByAdministrativeUnit(foundAdmUnit);
        List<ProvinceDto> dtoList = list.stream().map(province -> mapper.map(province, ProvinceDto.class)).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public List<ProvinceDto> getAllByAdministrativeRegionId(int admRegionId) {
        AdministrativeRegion foundAdmRegion = administrativeRegionRepository.findById(admRegionId).orElseThrow(
                () -> new ResourceNotFoundException("AdministrativeRegion", "AdministrativeRegionID", String.valueOf(admRegionId))
        );
        List<Province> list = repository.findAllByAdministrativeRegion(foundAdmRegion);
        List<ProvinceDto> dtoList = list.stream().map(province -> mapper.map(province, ProvinceDto.class)).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public ProvinceDto createProvince(Map<String, Object> JSONInfoAsMap) {
        boolean isAllPropertiesFound = JSONInfoAsMap.containsKey("id") &&
                JSONInfoAsMap.containsKey("name") &&
                JSONInfoAsMap.containsKey("administrativeRegionId") &&
                JSONInfoAsMap.containsKey("administrativeUnitId");
        if (!isAllPropertiesFound //kiem tra neu AdministrativeUnitsID dung voi level
            || (Integer) JSONInfoAsMap.get("administrativeUnitId") != Utils.AdministrativeUnitsLv1.PROVINCE.getId()
            || (Integer) JSONInfoAsMap.get("administrativeUnitId") != Utils.AdministrativeUnitsLv1.MUNICIPALITY.getId()) {
            return null;
        }
        String id = (String) JSONInfoAsMap.get("code");
        Optional<Province> foundProvince = repository.findById(id);
        if (foundProvince.isEmpty()) {
            logger.trace("Found no such Id. Safe to save");
            Province newProvince = new Province();
            administrativeRegionRepository.findById((Integer) JSONInfoAsMap.get("administrativeRegionId")).ifPresent(foundAdmRegion -> {
                administrativeUnitRepository.findById((Integer) JSONInfoAsMap.get("administrativeUnitId")).ifPresent(foundAdmUnit -> {
                    //kiem tra neu AdministrativeUnitsID dung voi level
                    newProvince.setName((String) JSONInfoAsMap.get("name"));
                    newProvince.setAdministrativeRegion(foundAdmRegion);
                    newProvince.setAdministrativeUnit(foundAdmUnit);
                    logger.trace("set things done");
                    repository.save(newProvince);
                });
            });

            logger.trace("provinceCreated");
            return mapper.map(newProvince, ProvinceDto.class);
        }
        return null;
    }

    @Override
    public ProvinceDto updateProvince(String provinceIdNeedUpdate, ProvinceDto province) {
        Province foundProvince = repository.findById(provinceIdNeedUpdate).orElseThrow(
                () -> new ResourceNotFoundException("Province", "ProvinceCode", provinceIdNeedUpdate));

        String provinceCode = province.getCode();
        if (!foundProvince.equals(provinceCode)) {
            repository.findById(provinceCode).ifPresent(
                    p -> {throw new ResourceFoundException("Province", "ProvinceCode", provinceCode);}
            );
        }
        int administrativeUnitsID = province.getAdministrativeUnit().getId();
        AdministrativeUnit foundAdmUnit = administrativeUnitRepository.findById(administrativeUnitsID).orElseThrow(
                ()-> new ResourceNotFoundException("AdministrativeUnit", "AdministrativeUnitId", String.valueOf(administrativeUnitsID))
        );

        int admRegionID = province.getAdministrativeRegion().getId();
        AdministrativeRegion foundAdmRegion = administrativeRegionRepository.findById(admRegionID).orElseThrow(
                () -> new ResourceNotFoundException("AdministrativeRegion", "AdministrativeRegionId", String.valueOf(admRegionID))
        );

        boolean isAdministrativeUnitsLv1 = false;
        for (Utils.AdministrativeUnitsLv1 a : Utils.AdministrativeUnitsLv1.values()) {
            if (administrativeUnitsID == a.getId()) {
                isAdministrativeUnitsLv1 = true;
                break;
            }
        }

        if (isAdministrativeUnitsLv1) {
            Province provinceUpdate = new Province();
            provinceUpdate.setCode(provinceCode);
            provinceUpdate.setName(province.getName());
            provinceUpdate.setAdministrativeUnit(foundAdmUnit);
            provinceUpdate.setAdministrativeRegion(foundAdmRegion);
            return mapper.map(repository.save(provinceUpdate), ProvinceDto.class);
        }
        return null;
    }

    public ProvinceDto updateProvince(String provinceIdNeedUpdate, Map<String, Object> JSONInfoAsMap) {
        Optional<Province> foundProvince = repository.findById(provinceIdNeedUpdate);
        if ((Integer) JSONInfoAsMap.get("administrativeUnitId") != Utils.AdministrativeUnitsLv1.PROVINCE.getId()
                || (Integer) JSONInfoAsMap.get("administrativeUnitId") != Utils.AdministrativeUnitsLv1.MUNICIPALITY.getId()) {
            return null;
        }
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
                        administrativeUnitRepository.findById((Integer) JSONInfoAsMap.get("administrativeUnitId"))
                                .ifPresent(provinceNeedChange::setAdministrativeUnit);
                        logger.trace("admUnit changed");
                        break;
                    }
                    case "administrativeRegionId": {
                        administrativeRegionRepository.findById((Integer) JSONInfoAsMap.get("administrativeRegionId"))
                                .ifPresent(provinceNeedChange::setAdministrativeRegion);
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
        repository.delete(repository.findById(provinceId)
                .orElseThrow(() -> new ResourceNotFoundException("Province", "provinceId", provinceId)));
        return "Deleted";
    }
}
