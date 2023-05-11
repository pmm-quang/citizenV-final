package com.citizenv.app.service.impl;

import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.AdministrativeRegion;
import com.citizenv.app.entity.AdministrativeUnit;
import com.citizenv.app.entity.Province;
import com.citizenv.app.exception.InvalidArgumentException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
    public Map<String, Object> getAll(int page) {
        Page<Province> provinceEntities = repository.findAll(PageRequest.of(page - 1, 15));
        List<ProvinceDto> list = provinceEntities.stream().map(l-> mapper.map(l, ProvinceDto.class)).collect(Collectors.toList());
        Map<String, Object> res = new HashMap<>();
        res.put("totalPages", provinceEntities.getTotalPages());
        res.put("page", page);
        res.put("pageElements", provinceEntities.getNumberOfElements());
        res.put("provinces", list);
        return res;
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
        boolean isAllPropertiesFound = JSONInfoAsMap.containsKey("code") &&
                JSONInfoAsMap.containsKey("name") &&
                JSONInfoAsMap.containsKey("administrativeRegionId") &&
                JSONInfoAsMap.containsKey("administrativeUnitId") &&
                JSONInfoAsMap.containsKey("administrativeCode");
        if (!isAllPropertiesFound) {
            System.out.println("u fucked up");
            throw new InvalidArgumentException();
        }
        if (!Utils.AdministrativeUnitsLv1.containsKey((Integer) JSONInfoAsMap.get("administrativeUnitId"))) {
            System.out.println("u fucked up too");
            throw new InvalidArgumentException();
        }

        String code = (String) JSONInfoAsMap.get("code");
        String admCode = (String) JSONInfoAsMap.get("administrativeCode");
        Optional<Province> existedProvinceByCode = repository.findById(code);
        Optional<Province> existedProvinceByAdmCode = repository.findByAdministrativeCode(admCode);

        if (existedProvinceByCode.isPresent()) {
            throw new ResourceFoundException("Province", "code", code);
        }

        if (existedProvinceByAdmCode.isPresent()) {
            throw new ResourceFoundException("Province", "administrativeCode", admCode);
        }

        System.out.println("Found no such Id. Safe to save");
        Province newProvince = new Province();
        administrativeRegionRepository.findById((Integer) JSONInfoAsMap.get("administrativeRegionId")).ifPresent(foundAdmRegion -> {
            administrativeUnitRepository.findById((Integer) JSONInfoAsMap.get("administrativeUnitId")).ifPresent(foundAdmUnit -> {
                newProvince.setCode((String) JSONInfoAsMap.get("code"));
                newProvince.setName((String) JSONInfoAsMap.get("name"));
                newProvince.setAdministrativeRegion(foundAdmRegion);
                newProvince.setAdministrativeUnit(foundAdmUnit);
                newProvince.setAdministrativeCode((String) JSONInfoAsMap.get("administrativeCode"));
                System.out.println("set things done");
                repository.save(newProvince);
            });
        });

        logger.info("provinceCreated");
        return mapper.map(newProvince, ProvinceDto.class);
    }

    @Override
    public ProvinceDto updateProvince(String provinceIdNeedUpdate, ProvinceDto province) {
        Province foundProvince = repository.findById(provinceIdNeedUpdate).orElseThrow(
                () -> new ResourceNotFoundException("Province", "ProvinceCode", provinceIdNeedUpdate));

        String provinceCode = province.getCode();
        if (!provinceIdNeedUpdate.equals(provinceCode)) {
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
            if (administrativeUnitsID == a.getValue()) {
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
        if (foundProvince.isPresent()) {
            Province provinceNeedChange = foundProvince.get();
            System.out.println("Found the thing. Updating...");
            for (String property :
                    JSONInfoAsMap.keySet()) {
                switch (property) {
                    case "code": continue;
                    case "name": {
                        provinceNeedChange.setName((String) JSONInfoAsMap.get(property));
                        System.out.println("Name changed");
                        break;
                    }
                    case "administrativeUnitId": {
                        if (!Utils.AdministrativeUnitsLv1.containsKey((Integer) JSONInfoAsMap.get("administrativeUnitId"))) {
                            throw new InvalidArgumentException();
                        }
                        administrativeUnitRepository.findById((Integer) JSONInfoAsMap.get("administrativeUnitId"))
                                .ifPresent(provinceNeedChange::setAdministrativeUnit);
                        System.out.println("admUnit changed");
                        break;
                    }
                    case "administrativeRegionId": {
                        administrativeRegionRepository.findById((Integer) JSONInfoAsMap.get("administrativeRegionId"))
                                .ifPresent(provinceNeedChange::setAdministrativeRegion);
                        System.out.println("admRegion changed");
                        break;
                    }
                }
            }
            repository.save(provinceNeedChange);
            System.out.println("provinceUpdated");
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
