package com.citizenv.app.service.impl;

import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.AdministrativeRegion;
import com.citizenv.app.entity.AdministrativeUnit;
import com.citizenv.app.entity.Province;
import com.citizenv.app.exception.InvalidArgumentException;
import com.citizenv.app.exception.InvalidException;
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
import org.springframework.transaction.annotation.Transactional;

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
    public ProvinceDto getById(Long provinceId) {
        Province province = repository.findById(provinceId).orElseThrow(
                () -> new ResourceNotFoundException("Province", "provinceId", String.valueOf(provinceId)));
        return mapper.map(province, ProvinceDto.class);
    }

    @Override
    public ProvinceDto getByCode(String code) {
        Province foundProvince = repository.findByCode(code).orElseThrow(
                () -> new ResourceNotFoundException("Province", "provinceCode", code)
        );
        return mapper.map(foundProvince, ProvinceDto.class);
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
    public ProvinceDto createProvince(ProvinceDto province) {
        String provinceCode = province.getCode();
        repository.findByCode(provinceCode).ifPresent(p -> {
            throw new ResourceFoundException("Province", "ProvinceCode", provinceCode);
        });

        Map<String, Object> map = validate(province);
        Province newProvince = mapper.map(province, Province.class);
        newProvince.setAdministrativeUnit((AdministrativeUnit) map.get("admUnit"));
        newProvince.setAdministrativeRegion((AdministrativeRegion) map.get("admRegion"));
        Province createProvince = repository.save(newProvince);
        return mapper.map(createProvince, ProvinceDto.class);
    }

    @Transactional
    @Override
    public ProvinceDto updateProvince(String provinceCodeNeedUpdate, ProvinceDto province) {
        Province foundProvince = repository.findByCode(provinceCodeNeedUpdate).orElseThrow(
                () -> new ResourceNotFoundException("Province", "ProvinceCode", provinceCodeNeedUpdate));

        String provinceCode = province.getCode();
        if (!provinceCodeNeedUpdate.equals(provinceCode)) {
            repository.findByCode(provinceCode).ifPresent(
                    p -> {throw new ResourceFoundException("Province", "ProvinceCode", provinceCode);}
            );
        }

        Map<String, Object> map = validate(province);
        AdministrativeUnit foundAdmUnit = (AdministrativeUnit) map.get("admUnit");
        AdministrativeRegion foundAdmRegion = (AdministrativeRegion) map.get("admRegion");
        foundProvince.setCode(provinceCode);
        foundProvince.setName(province.getName());
        foundProvince.setAdministrativeRegion(foundAdmRegion);
        foundProvince.setAdministrativeUnit(foundAdmUnit);
//        mapper.map(province, foundProvince);
//        foundProvince.setAdministrativeUnit(foundAdmUnit);
//        foundProvince.setAdministrativeRegion(foundAdmRegion);
        return mapper.map(foundProvince, ProvinceDto.class);
    }


    public String deleteById(Long provinceId) {
        repository.delete(repository.findById(provinceId)
                .orElseThrow(() -> new ResourceNotFoundException("Province", "provinceId", String.valueOf(provinceId))));
        return "Deleted";
    }

    private Map<String, Object> validate(ProvinceDto province) {
        Integer admUnitId = province.getAdministrativeUnit().getId();
        AdministrativeUnit admUnit = administrativeUnitRepository.findById(admUnitId)
                .orElseThrow(() -> new ResourceNotFoundException("AdministrativeUnit", "AdministrativeUnitId", String.valueOf(admUnitId)));
        Integer admRegionId = province.getAdministrativeRegion().getId();
        AdministrativeRegion admRegion = administrativeRegionRepository.findById(admRegionId)
                .orElseThrow(() -> new ResourceNotFoundException("AdministrativeRegion", "AdministrativeRegionId", String.valueOf(admRegionId)));

//        if(!Utils.validateName(province.getName())) {
//            throw new InvalidException("Ten khong dung dinh dang");
//        }
        if (!Utils.AdministrativeUnitsLv1.containsKey(admUnitId)) {
            throw new InvalidException("Don vi hanh chinh khong hop le");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("admUnit", admUnit);
        map.put("admRegion", admRegion);
        return map;
    }
}
