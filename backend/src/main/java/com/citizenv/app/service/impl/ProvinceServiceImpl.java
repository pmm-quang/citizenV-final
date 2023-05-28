package com.citizenv.app.service.impl;

import com.citizenv.app.component.Constant;
import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.AdministrativeRegion;
import com.citizenv.app.entity.AdministrativeUnit;
import com.citizenv.app.entity.Province;
import com.citizenv.app.exception.InvalidArgumentException;
import com.citizenv.app.exception.InvalidException;
import com.citizenv.app.exception.ResourceFoundException;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.ProvinceDto;
import com.citizenv.app.repository.AdministrativeDivisionRepository;
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
    private final ModelMapper mapper;
    private final AdministrativeRegionRepository administrativeRegionRepository;
    private final AdministrativeUnitRepository administrativeUnitRepository;
    private final ProvinceRepository repository;
    private final AdministrativeDivisionRepository administrativeDivisionRepository;

    public ProvinceServiceImpl(ModelMapper mapper, AdministrativeRegionRepository administrativeRegionRepository, AdministrativeUnitRepository administrativeUnitRepository, ProvinceRepository repository, AdministrativeDivisionRepository administrativeDivisionRepository) {
        this.mapper = mapper;
        this.administrativeRegionRepository = administrativeRegionRepository;
        this.administrativeUnitRepository = administrativeUnitRepository;
        this.repository = repository;
        this.administrativeDivisionRepository = administrativeDivisionRepository;
    }

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
                () -> new ResourceNotFoundException("Tỉnh/thành phố", "mã định danh", String.valueOf(provinceId)));
        return mapper.map(province, ProvinceDto.class);
    }

    @Override
    public ProvinceDto getByCode(String code) {
        Province foundProvince = repository.findByCode(code).orElseThrow(
                () -> new ResourceNotFoundException("Tỉnh/thành phố", "mã định danh", code)
        );
        return mapper.map(foundProvince, ProvinceDto.class);
    }

    @Override
    public List<ProvinceDto> getAllByAdministrativeUnitId(int admUnitId) {
        AdministrativeUnit foundAdmUnit = administrativeUnitRepository.findById(admUnitId).orElseThrow(
                () -> new ResourceNotFoundException("Đơn vị hành chính", "id",String.valueOf(admUnitId))
        );
        List<Province> list = repository.findAllByAdministrativeUnit(foundAdmUnit);
        List<ProvinceDto> dtoList = list.stream().map(province -> mapper.map(province, ProvinceDto.class)).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public List<ProvinceDto> getAllByAdministrativeRegionId(int admRegionId) {
        AdministrativeRegion foundAdmRegion = administrativeRegionRepository.findById(admRegionId).orElseThrow(
                () -> new ResourceNotFoundException("Khu vực hành chính", "id", String.valueOf(admRegionId))
        );
        List<Province> list = repository.findAllByAdministrativeRegion(foundAdmRegion);
        List<ProvinceDto> dtoList = list.stream().map(province -> mapper.map(province, ProvinceDto.class)).collect(Collectors.toList());
        return dtoList;
    }

    @Transactional
    @Override
    public String createProvince(ProvinceDto province) {
        String provinceCode = province.getCode();
        repository.findByCode(provinceCode).ifPresent(p -> {
            throw new ResourceFoundException("Tỉnh/thành phố", "mã định danh", provinceCode);
        });

        Map<String, Object> map = validate(province);

        List<Province> list = repository.findAll();
        for (Province p: list) {
            if (p.getName().equals(province.getName())) {
                throw new InvalidException(Constant.ERR_MESSAGE_UNIT_NAME_ALREADY_EXISTS);
            }
        }

        Province newProvince = mapper.map(province, Province.class);
        newProvince.setAdministrativeUnit((AdministrativeUnit) map.get("admUnit"));
        newProvince.setAdministrativeRegion((AdministrativeRegion) map.get("admRegion"));
        Province createProvince = repository.save(newProvince);
        return "Tạo mới tỉnh/thành phố thành công";
    }

    @Transactional
    @Override
    public String updateProvince(String provinceCodeNeedUpdate, ProvinceDto province) {
        Province foundProvince = repository.findByCode(provinceCodeNeedUpdate).orElseThrow(
                () -> new ResourceNotFoundException("Tỉnh/thành phố", "mã định danh", provinceCodeNeedUpdate));

        String provinceCode = province.getCode();
        if (!provinceCodeNeedUpdate.equals(provinceCode)) {
            repository.findByCode(provinceCode).ifPresent(
                    p -> {throw new ResourceFoundException("Tỉnh/thành phố", "mã định danh", provinceCode);}
            );
        }

        if (!foundProvince.getName().equals(province.getName())) {
            repository.findByName(province.getName()).ifPresent(
                    province1 -> {throw new InvalidException(Constant.ERR_MESSAGE_UNIT_NAME_ALREADY_EXISTS);}
            );
        }
        Map<String, Object> map = validate(province);
        AdministrativeUnit foundAdmUnit = (AdministrativeUnit) map.get("admUnit");
        AdministrativeRegion foundAdmRegion = (AdministrativeRegion) map.get("admRegion");
        foundProvince.setCode(provinceCode);
        foundProvince.setName(province.getName());
        foundProvince.setAdministrativeRegion(foundAdmRegion);
        foundProvince.setAdministrativeUnit(foundAdmUnit);

        if (!provinceCodeNeedUpdate.equals(provinceCode)) {
            administrativeDivisionRepository.updateCodeOfSubDivision(provinceCode, 3, provinceCodeNeedUpdate);
        }
        return "Chỉnh sửa tỉnh/thành phố thành công!";
    }


    @Transactional
    public String deleteById(Long provinceId) {
        repository.delete(repository.findById(provinceId)
                .orElseThrow(() -> new ResourceNotFoundException("Tỉnh/thành phố", "mã định danh", String.valueOf(provinceId))));
        return "Deleted";
    }

    private Map<String, Object> validate(ProvinceDto province) {
        Integer admUnitId = province.getAdministrativeUnit().getId();
        AdministrativeUnit admUnit = administrativeUnitRepository.findById(admUnitId)
                .orElseThrow(() -> new ResourceNotFoundException("Đơn vị hành chính", "id", String.valueOf(admUnitId)));
        Integer admRegionId = province.getAdministrativeRegion().getId();
        AdministrativeRegion admRegion = administrativeRegionRepository.findById(admRegionId)
                .orElseThrow(() -> new ResourceNotFoundException("Khu vực hành chính", "id", String.valueOf(admRegionId)));

//        if(!Utils.validateName(province.getName())) {
//            throw new InvalidException("Ten khong dung dinh dang");
//        }
        if (!Utils.validateFormatDivisionCode(province.getCode())) {
            throw new InvalidException(Constant.ERR_MESSAGE_UNIT_CODE_INVALID);
        }

        if (!Utils.AdministrativeUnitsLv1.containsKey(admUnitId)) {
            throw new InvalidException(Constant.ERR_MESSAGE_ADMINISTRATIVE_UNIT_INVALID);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("admUnit", admUnit);
        map.put("admRegion", admRegion);
        return map;
    }
}
