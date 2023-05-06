package com.citizenv.app.service.impl;

import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.AdministrativeUnit;
import com.citizenv.app.entity.District;
import com.citizenv.app.entity.Province;
import com.citizenv.app.exception.ResourceFoundException;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.DistrictDto;
import com.citizenv.app.repository.AdministrativeUnitRepository;
import com.citizenv.app.repository.DistrictRepository;
import com.citizenv.app.repository.ProvinceRepository;
import com.citizenv.app.service.DistrictService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DistrictServiceImpl implements DistrictService {
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private DistrictRepository repository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private AdministrativeUnitRepository administrativeUnitRepository;

    public List<DistrictDto> getAll() {
        List<District> entities = repository.findAll();
        return entities.stream().map(l-> mapper.map(l, DistrictDto.class)).collect(Collectors.toList());
    }

    public DistrictDto getById(String districtId) {
        District district = repository.findById(districtId).orElseThrow(
                () -> new ResourceNotFoundException("District", "DistrictCode", " " + districtId));
        return mapper.map(district, DistrictDto.class);
    }

    @Override
    public List<DistrictDto> getAllByProvinceCode(String provinceCode) {
        Province foundProvince = provinceRepository.findById(provinceCode).orElseThrow(
                () -> new ResourceNotFoundException("Province", "ProvinceCode", provinceCode)
        );
        List<District> list = repository.findAllByProvince(foundProvince);
        List<DistrictDto> dtoList = list.stream().map(district -> mapper.map(district, DistrictDto.class)).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public List<DistrictDto> getAllByAdministrativeUnitId(int admUnitId) {
        AdministrativeUnit foundAdmUnit = administrativeUnitRepository.findById(admUnitId).orElseThrow(
                ()-> new ResourceNotFoundException("AdministrativeUnit", "AdministrativeUnitId", String.valueOf(admUnitId))
        );
        List<District> list = repository.findAllByAdministrativeUnit(foundAdmUnit);
        List<DistrictDto> dtoList = list.stream().map(district -> mapper.map(district, DistrictDto.class)).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public DistrictDto createDistrict(Map<String, Object> JSONInfoAsMap) {
        boolean isAllPropertiesFound = JSONInfoAsMap.containsKey("code") &&
                JSONInfoAsMap.containsKey("name") &&
                JSONInfoAsMap.containsKey("provinceId") &&
                JSONInfoAsMap.containsKey("administrativeUnitId");
        if (!isAllPropertiesFound) {
            return  null;
        }

        boolean isAdministrativeUnitsLv2 = false;
        for (Utils.AdministrativeUnitsLv2 a : Utils.AdministrativeUnitsLv2.values()) {
            if (JSONInfoAsMap.get("administrativeUnitId") == a) {
                isAdministrativeUnitsLv2 = true;
                break;
            }
        }
        if (!isAdministrativeUnitsLv2) {
            return null;
        }
        String id = (String) JSONInfoAsMap.get("code");
        Optional<District> foundDistrict = repository.findById(id);
        if (foundDistrict.isEmpty()) {
            District newDistrict = new District();
            provinceRepository.findById((String) JSONInfoAsMap.get("provinceId")).ifPresent(foundProvince -> {
                administrativeUnitRepository.findById((Integer) JSONInfoAsMap.get("administrativeUnitId")).ifPresent(foundAdmUnit -> {
                    newDistrict.setCode(id);
                    newDistrict.setName((String) JSONInfoAsMap.get("name"));
                    newDistrict.setProvince(foundProvince);
                    newDistrict.setAdministrativeUnit(foundAdmUnit);
                    repository.save(newDistrict);
                });
            });
            return mapper.map(newDistrict, DistrictDto.class);
        }
        return null;
    }

    @Override
    public DistrictDto createDistrict(DistrictDto district) {
        String newDistrictCode = district.getCode();
        repository.findById(newDistrictCode).ifPresent(
                foundDistrict -> {throw new ResourceFoundException("District", "DistrictCode", newDistrictCode);});

        String provinceCode = district.getProvince().getCode();
        Province foundProvince = provinceRepository.findById(provinceCode).orElseThrow(
                ()-> new ResourceNotFoundException("Province", "ProvinceCode", provinceCode)
        );

        int admUnitId = district.getAdministrativeUnit().getId();
        AdministrativeUnit foundAdmUnit = administrativeUnitRepository.findById(admUnitId).orElseThrow(
                () -> new ResourceNotFoundException("AdministrativeUnit", "AdministrativeUnitId", String.valueOf(admUnitId))
        );

        boolean isAdministrativeUnitsLv2 = false;
        int administrativeUnitsID = district.getAdministrativeUnit().getId();
        for (Utils.AdministrativeUnitsLv2 a : Utils.AdministrativeUnitsLv2.values()) {
            if (a.getValue() == administrativeUnitsID) {
                isAdministrativeUnitsLv2 = true;
                break;
            }
        }

        if (newDistrictCode.indexOf(provinceCode) == 0 && isAdministrativeUnitsLv2) {
            District newDistrict = new District();
            newDistrict.setCode(newDistrictCode);
            newDistrict.setName(district.getName());
            newDistrict.setProvince(foundProvince);
            newDistrict.setAdministrativeUnit(foundAdmUnit);
            return mapper.map(repository.save(newDistrict), DistrictDto.class);
        }
        return null;

    }

    @Override
    public DistrictDto updateDistrict(String districtCodeNeedUpdate, DistrictDto district) {
        String districtCode = district.getCode();
        District foundDistrict = repository.findById(districtCodeNeedUpdate).orElseThrow(
                () -> new ResourceNotFoundException("District", "DistrictCode", districtCodeNeedUpdate)
        );

        if (!districtCode.equals(districtCodeNeedUpdate)) {
            repository.findById(districtCode).ifPresent(
                    fd -> {throw new ResourceFoundException("District", "DistrictCodeUpdate", districtCode);}
            );
        }

        int admUnitId = district.getAdministrativeUnit().getId();
        AdministrativeUnit foundAdmUnit = administrativeUnitRepository.findById(admUnitId).orElseThrow(
                () -> new ResourceNotFoundException("AdministrativeUnit", "AdministrativeUnitID", String.valueOf(admUnitId))
        );

        String provinceCode = district.getProvince().getCode();
        Province foundProvince = provinceRepository.findById(provinceCode).orElseThrow(
                ()-> new ResourceNotFoundException("Province", "ProvinceCode", provinceCode)
        );

        boolean isAdministrativeUnitsLv2 = false;
        int administrativeUnitsID = district.getAdministrativeUnit().getId();
        for (Utils.AdministrativeUnitsLv2 a : Utils.AdministrativeUnitsLv2.values()) {
            if (a.getValue() == administrativeUnitsID) {
                isAdministrativeUnitsLv2 = true;
                break;
            }
        }

        if (districtCode.indexOf(provinceCode) == 0 && isAdministrativeUnitsLv2) {
           foundDistrict.setCode(districtCode);
           foundDistrict.setName(district.getName());
           foundDistrict.setProvince(foundProvince);
           foundDistrict.setAdministrativeUnit(foundAdmUnit);
           return mapper.map(repository.save(foundDistrict), DistrictDto.class);
        }
        return null;
    }

    @Override
    public DistrictDto updateDistrict(String districtIdNeedUpdate, Map<String, Object> JSONInfoAsMap) {
        Optional<District> foundDistrict = repository.findById(districtIdNeedUpdate);
        boolean isAdministrativeUnitsLv2 = false;
        for (Utils.AdministrativeUnitsLv2 a : Utils.AdministrativeUnitsLv2.values()) {
            if (JSONInfoAsMap.get("administrativeUnitId") == a) {
                isAdministrativeUnitsLv2 = true;
                break;
            }
        }
        if (!isAdministrativeUnitsLv2) {
            return null;
        }
        if (foundDistrict.isPresent()) {
            District districtNeedChange = new District();
            for (String property : JSONInfoAsMap.keySet()) {
                switch (property) {
                    case "code" :
                        districtNeedChange.setCode((String) JSONInfoAsMap.get("code"));
                        break;
                    case "name" :
                        districtNeedChange.setName((String) JSONInfoAsMap.get("name"));
                        break;
                    case "administrativeUnitId" :
                        administrativeUnitRepository.findById((Integer) JSONInfoAsMap.get("administrativeUnitId"))
                                .ifPresent(districtNeedChange::setAdministrativeUnit);
                        break;
                    case "provinceId" :
                        provinceRepository.findById((String) JSONInfoAsMap.get("provinceId"))
                                .ifPresent(districtNeedChange::setProvince);
                        break;
                }
            }
            repository.save(districtNeedChange);
            return mapper.map(districtIdNeedUpdate, DistrictDto.class);
        }
        return null;
    }
}
