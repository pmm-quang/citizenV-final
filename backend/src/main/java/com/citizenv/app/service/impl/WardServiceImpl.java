package com.citizenv.app.service.impl;

import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.AdministrativeUnit;
import com.citizenv.app.entity.District;
import com.citizenv.app.entity.Ward;
import com.citizenv.app.exception.InvalidException;
import com.citizenv.app.exception.ResourceFoundException;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.WardDto;
import com.citizenv.app.payload.custom.CustomWardRequest;
import com.citizenv.app.repository.AdministrativeUnitRepository;
import com.citizenv.app.repository.DistrictRepository;
import com.citizenv.app.repository.WardRepository;
import com.citizenv.app.service.WardService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
public class WardServiceImpl implements WardService {
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private WardRepository repo;

    @Autowired
    private DistrictRepository districtRepo;

    @Autowired
    private AdministrativeUnitRepository admUnitRepo;

    @Override
    public List<WardDto> getAll() {
        List<Ward> entities = repo.findAll();
        return entities.stream().map(l-> mapper.map(l, WardDto.class)).collect(Collectors.toList());
    }

    @Override
    public WardDto getByCode(String code) {
        Ward foundWard = repo.findByCode(code).orElseThrow(
                () -> new ResourceNotFoundException("Ward", "WardCode", code)
        );
        return mapper.map(foundWard, WardDto.class);
    }

    @Override
    public List<WardDto> getByDistrictCode(String districtCode) {
        District foundDistrict = districtRepo.findByCode(districtCode).orElseThrow(
                () -> new ResourceNotFoundException("District", "DistrictCode", districtCode)
        );
        List<Ward> list = repo.findAllByDistrict(foundDistrict);
        List<WardDto> dtoList = list.stream().map(ward -> mapper.map(ward, WardDto.class)).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public List<WardDto> getByAdministrativeUnitId(int admId) {
        AdministrativeUnit foundAdmUnit = admUnitRepo.findById(admId).orElseThrow(
                () -> new ResourceNotFoundException("AdministrativeUnit", "AdministrativeUnitID", String.valueOf(admId))
        );
        List<Ward> list = repo.findAllByAdministrativeUnit(foundAdmUnit);
        List<WardDto> dtoList = list.stream().map(ward -> mapper.map(ward, WardDto.class)).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public WardDto createWard(String wardCode,String districtCode, WardDto ward) {

        return null;
    }

    @Override
    public WardDto createWard(WardDto ward) {
        String wardCode = ward.getCode();
        repo.findByCode(wardCode).ifPresent(
                w -> {throw new ResourceFoundException("Ward", "WardCode", wardCode);}
        );
        Ward createWard = validate(ward);
        Ward newWard = repo.save(createWard);
        return mapper.map(newWard, WardDto.class);
    }

    @Override
    public WardDto createWard(CustomWardRequest ward) {
        repo.findByCode(ward.getCode()).ifPresent(w -> {
            throw new ResourceFoundException("Ward", "WardCode", ward.getCode());
        });
        Ward createWard = validate(ward.getCode(), ward.getName(), ward.getDistrictCode(), ward.getAdministrativeUnitId());
        Ward newWard = repo.save(createWard);
        return mapper.map(newWard, WardDto.class);
    }
    @Transactional
    @Override
    public WardDto updateWard(String wardNeedUpdateID, WardDto ward) {
        Ward foundWard = repo.findByCode(wardNeedUpdateID).orElseThrow(
                () -> new ResourceNotFoundException("Ward", "WardCode", wardNeedUpdateID)
        );
        String wardCode = ward.getCode();
        if (!wardCode.equals(wardNeedUpdateID)) {
            repo.findByCode(wardCode).ifPresent(
                    w -> {throw new ResourceFoundException("Ward", "WardCode", wardCode);}
            );
        }
        Ward createWard = validate(ward.getCode(), ward.getName(), ward.getDistrict().getCode(), ward.getAdministrativeUnit().getId());
        foundWard.setName(createWard.getName());
        foundWard.setCode(createWard.getCode());
        foundWard.setAdministrativeUnit(createWard.getAdministrativeUnit());
        foundWard.setDistrict(createWard.getDistrict());
        return mapper.map(foundWard, WardDto.class);
    }

    @Transactional
    @Override
    public WardDto updateWard(String wardNeedUpdateCode, CustomWardRequest ward) {
        Ward foundWard = repo.findByCode(wardNeedUpdateCode).orElseThrow(
                () -> new ResourceNotFoundException("Ward", "WardCode", wardNeedUpdateCode)
        );
        String wardCode = ward.getCode();
        if (!wardCode.equals(wardNeedUpdateCode)) {
            repo.findByCode(wardCode).ifPresent(
                    w -> {throw new ResourceFoundException("Ward", "WardCode", wardCode);}
            );
        }
        Ward createWard = validate(ward);
        foundWard.setCode(createWard.getCode());
        foundWard.setName(createWard.getName());
        foundWard.setAdministrativeUnit(createWard.getAdministrativeUnit());
        foundWard.setDistrict(createWard.getDistrict());
        return mapper.map(foundWard, WardDto.class);
    }

    private Ward validate(WardDto ward) {
        String wardCode = ward.getCode();
        String wardName = ward.getName();
        String districtCode = ward.getDistrict().getCode();
        Integer admUnitId = ward.getAdministrativeUnit().getId();
        AdministrativeUnit foundAdmUnit = admUnitRepo.findById(admUnitId).orElseThrow(
                () -> new ResourceNotFoundException("AdministrativeUnit","AdministrativeUnitId", String.valueOf(admUnitId))
        );

        District foundDistrict = districtRepo.findByCode(districtCode).orElseThrow(
                () -> new ResourceNotFoundException("District", "DistrictCode", districtCode)
        );

        if (!Utils.AdministrativeUnitsLv3.containsKey(admUnitId)) {
            throw new InvalidException("Ma don vi hanh chinh khong hop le");
        }
        if (wardCode.indexOf(districtCode) != 0) {
            throw new InvalidException("Ma don vi khong hop le");
        }
//        if (!Utils.validateName(wardName)) {
//            throw new InvalidException("Ten don vi khong hop le");
//        }
        Map<String, Object> map = new HashMap<>();
        map.put("admUnit", foundAdmUnit);
        map.put("district", foundDistrict);
        Ward newWard = mapper.map(ward, Ward.class);
        newWard.setDistrict(foundDistrict);
        newWard.setAdministrativeUnit(foundAdmUnit);
        return newWard;
    }
    private Ward validate(CustomWardRequest ward) {
        String wardCode = ward.getCode();
        String wardName = ward.getName();
        String districtCode = ward.getDistrictCode();
        Integer admUnitId = ward.getAdministrativeUnitId();
        AdministrativeUnit foundAdmUnit = admUnitRepo.findById(admUnitId).orElseThrow(
                () -> new ResourceNotFoundException("AdministrativeUnit","AdministrativeUnitId", String.valueOf(admUnitId))
        );

        District foundDistrict = districtRepo.findByCode(districtCode).orElseThrow(
                () -> new ResourceNotFoundException("District", "DistrictCode", districtCode)
        );

        if (!Utils.AdministrativeUnitsLv3.containsKey(admUnitId)) {
            throw new InvalidException("Ma don vi hanh chinh khong hop le");
        }
        if (wardCode.indexOf(districtCode) != 0) {
            throw new InvalidException("Ma don vi khong hop le");
        }
//        if (!Utils.validateName(wardName)) {
//            throw new InvalidException("Ten don vi khong hop le");
//        }
        Ward newWard = new Ward();
        newWard.setCode(wardCode);
        newWard.setName(wardName);
        newWard.setDistrict(foundDistrict);
        newWard.setAdministrativeUnit(foundAdmUnit);
        return newWard;
    }
    private Ward validate(String wardCode, String wardName, String districtCode, Integer admUnitId) {
        AdministrativeUnit foundAdmUnit = admUnitRepo.findById(admUnitId).orElseThrow(
                () -> new ResourceNotFoundException("AdministrativeUnit","AdministrativeUnitId", String.valueOf(admUnitId))
        );

        District foundDistrict = districtRepo.findByCode(districtCode).orElseThrow(
                () -> new ResourceNotFoundException("District", "DistrictCode", districtCode)
        );

        if (!Utils.AdministrativeUnitsLv3.containsKey(admUnitId)) {
            throw new InvalidException("Ma don vi hanh chinh khong hop le");
        }
        if (wardCode.indexOf(districtCode) != 0) {
            throw new InvalidException("Ma don vi khong hop le");
        }
//        if (!Utils.validateName(wardName)) {
//            throw new InvalidException("Ten don vi khong hop le");
//        }
        Ward newWard = new Ward();
        newWard.setCode(wardCode);
        newWard.setName(wardName);
        newWard.setDistrict(foundDistrict);
        newWard.setAdministrativeUnit(foundAdmUnit);
        return newWard;
    }
}
