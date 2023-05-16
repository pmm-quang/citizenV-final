package com.citizenv.app.service.impl;

import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.AdministrativeUnit;
import com.citizenv.app.entity.District;
import com.citizenv.app.entity.Province;
import com.citizenv.app.exception.InvalidException;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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

    @Override
    public List<DistrictDto> getAll() {
        List<District> entities = repository.findAll();
        return entities.stream().map(l-> mapper.map(l, DistrictDto.class)).collect(Collectors.toList());
    }
    @Override
    public DistrictDto getById(Long districtId) {
        District district = repository.findById(districtId).orElseThrow(
                () -> new ResourceNotFoundException("District", "DistrictCode", " " + districtId));
        return mapper.map(district, DistrictDto.class);
    }

    @Override
    public DistrictDto getByCode(String code) {
        District district = repository.findByCode(code).orElseThrow(
                () -> new ResourceNotFoundException("District", "DistrictCode", code)
        );
        return mapper.map(district, DistrictDto.class);
    }

    @Override
    public List<DistrictDto> getAllByProvinceCode(String provinceCode) {
        Province foundProvince = provinceRepository.findByCode(provinceCode).orElseThrow(
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
    public DistrictDto createDistrict(DistrictDto district) {
        String newDistrictCode = district.getCode();
        repository.findByCode(newDistrictCode).ifPresent(
                foundDistrict -> {throw new ResourceFoundException("District", "DistrictCode", newDistrictCode);});


        Map<String, Object> map = vaidate(district);
        Province foundProvince = (Province) map.get("province");
        AdministrativeUnit foundAdmUnit = (AdministrativeUnit) map.get("admUnit");
        District createDistrict = mapper.map(district, District.class);
        createDistrict.setProvince(foundProvince);
        createDistrict.setAdministrativeUnit(foundAdmUnit);
        District newDistrict = repository.save(createDistrict);
        return mapper.map(newDistrict, DistrictDto.class);

    }

    @Transactional
    @Override
    public DistrictDto updateDistrict(String districtCodeNeedUpdate, DistrictDto district) {
        String districtCode = district.getCode();
        District foundDistrict = repository.findByCode(districtCodeNeedUpdate).orElseThrow(
                () -> new ResourceNotFoundException("District", "DistrictCode", districtCodeNeedUpdate)
        );

        if (!districtCode.equals(districtCodeNeedUpdate)) {
            repository.findByCode(districtCode).ifPresent(
                    fd -> {throw new ResourceFoundException("District", "DistrictCodeUpdate", districtCode);}
            );
        }

        Map<String, Object> map = vaidate(district);
        Province foundProvince = (Province) map.get("province");
        AdministrativeUnit foundAdmUnit = (AdministrativeUnit) map.get("admUnit");
        foundDistrict.setCode(district.getCode());
        foundDistrict.setName(district.getName());
        foundDistrict.setProvince(foundProvince);
        foundDistrict.setAdministrativeUnit(foundAdmUnit);
        return mapper.map(foundDistrict, DistrictDto.class);
    }
    @Override
    public void deleteDistrict(String districtCode) {

    }

    private boolean validate(DistrictDto district) {
        String name = district.getName();
        Integer admUnitId = district.getAdministrativeUnit().getId();
        String districtCode = district.getCode();
        String provinceCode = district.getProvince().getCode();
        if (!Utils.AdministrativeUnitsLv2.containsKey(admUnitId)) {
            throw new InvalidException("Ma don vi hanh chinh khong chinh xac");
        }
        if (districtCode.indexOf(provinceCode) != 0) {
            throw new InvalidException("Ma dơn vi khong hop le");
        }
//        if (!Utils.validateName(name)) {
//            throw new InvalidException("Ten khong hop le");
//        }
        return true;
    }

    private Map<String, Object> vaidate(DistrictDto district) {
        String name = district.getName();
        Integer admUnitId = district.getAdministrativeUnit().getId();
        String districtCode = district.getCode();
        String provinceCode = district.getProvince().getCode();
        if (!Utils.AdministrativeUnitsLv2.containsKey(admUnitId)) {
            throw new InvalidException("Ma don vi hanh chinh khong chinh xac");
        }
        if (districtCode.indexOf(provinceCode) != 0) {
            throw new InvalidException("Ma dơn vi khong hop le");
        }
//        if (!Utils.validateName(name)) {
//            throw new InvalidException("Ten khong hop le");
//        }
//        String provinceCode = district.getProvince().getCode();
        Province foundProvince = provinceRepository.findByCode(provinceCode).orElseThrow(
                ()-> new ResourceNotFoundException("Province", "ProvinceCode", provinceCode)
        );

//        int admUnitId = district.getAdministrativeUnit().getId();
        AdministrativeUnit foundAdmUnit = administrativeUnitRepository.findById(admUnitId).orElseThrow(
                () -> new ResourceNotFoundException("AdministrativeUnit", "AdministrativeUnitId", String.valueOf(admUnitId))
        );
        Map<String, Object> map = new HashMap<>();
        map.put("province", foundProvince);
        map.put("admUnit", foundAdmUnit);
        return map;
    }
}
