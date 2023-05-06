package com.citizenv.app.service.impl;

import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.AdministrativeUnit;
import com.citizenv.app.entity.District;
import com.citizenv.app.entity.Ward;
import com.citizenv.app.exception.ResourceFoundException;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.WardDto;
import com.citizenv.app.repository.AdministrativeUnitRepository;
import com.citizenv.app.repository.DistrictRepository;
import com.citizenv.app.repository.WardRepository;
import com.citizenv.app.service.WardService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
    public WardDto getById(String wardCode) {
       Ward foundWard = repo.findById(wardCode)
               .orElseThrow(() -> new ResourceNotFoundException("Ward", "WardCode",wardCode));
        return mapper.map(foundWard, WardDto.class);
    }

    @Override
    public List<WardDto> getByDistrictCode(String districtCode) {
        District foundDistrict = districtRepo.findById(districtCode).orElseThrow(
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
        Optional<Ward> foundWard = repo.findById(wardCode);
        if (foundWard.isEmpty()) {
            int administrativeUnitsID = ward.getAdministrativeUnit().getId();
            boolean isAdministrativeUnitsLv3 = false;
            for (Utils.AdministrativeUnitsLv3 a : Utils.AdministrativeUnitsLv3.values()) {
                if (a.getValue() == administrativeUnitsID) {
                    isAdministrativeUnitsLv3 = true;
                    break;
                }
            }
            if (!isAdministrativeUnitsLv3) {
                return null;
            }

            District foundDistrict = districtRepo.findById(districtCode)
                    .orElseThrow(() -> new ResourceNotFoundException("District", "DistrictCode", districtCode));
            Ward newWard = mapper.map(ward, Ward.class);
            repo.save(newWard);
            return mapper.map(newWard, WardDto.class);
        }
        return null;
    }

    @Override
    public WardDto createWard(WardDto ward) {
        String wardCode = ward.getCode();
        repo.findById(wardCode).ifPresent(
                w -> {throw new ResourceFoundException("Ward", "WardCode", wardCode);}
        );
        String districtCode = ward.getDistrict().getCode();
        District foundDistrict = districtRepo.findById(districtCode).orElseThrow(
                () -> new ResourceNotFoundException("District", "DistrictCode", districtCode)
        );
        int admUnitId = ward.getAdministrativeUnit().getId();
        AdministrativeUnit foundAdmUnit = admUnitRepo.findById(admUnitId).orElseThrow(
                () -> new ResourceNotFoundException("AdministrativeUnit", "AdministrativeUnitId", String.valueOf(admUnitId))
        );


        if (wardCode.indexOf(districtCode) == 0 && checkAdministrativeLv3(admUnitId)) {
            Ward newWard = new Ward();
            newWard.setCode(wardCode);
            newWard.setName(ward.getName());
            newWard.setDistrict(foundDistrict);
            newWard.setAdministrativeUnit(foundAdmUnit);

            return mapper.map(repo.save(newWard), WardDto.class);
        }
        return null;
    }

    @Transactional
    @Override
    public WardDto updateWard(String wardNeedUpdateID, WardDto ward) {
        Ward foundWard = repo.findById(wardNeedUpdateID).orElseThrow(
                () -> new ResourceNotFoundException("Ward", "WardCode", wardNeedUpdateID)
        );
        String wardCode = ward.getCode();
        if (!wardCode.equals(wardNeedUpdateID)) {
            repo.findById(wardCode).ifPresent(
                    w -> {throw new ResourceFoundException("Ward", "WardCode", wardCode);}
            );
        }
        String districtCode = ward.getDistrict().getCode();
        District foundDistrict = districtRepo.findById(districtCode).orElseThrow(
                () -> new ResourceNotFoundException("District", "DistrictCode", districtCode)
        );
        int admUnitId = ward.getAdministrativeUnit().getId();
        AdministrativeUnit foundAdmUnit = admUnitRepo.findById(admUnitId).orElseThrow(
                () -> new ResourceNotFoundException("AdministrativeUnit", "AdministrativeUnitId", String.valueOf(admUnitId))
        );

        if (wardCode.indexOf(districtCode) == 0 && checkAdministrativeLv3(admUnitId)) {
//            foundWard.setCode(wardCode);
//            foundWard.setName(ward.getName());
//            foundWard.setDistrict(foundDistrict);
//            foundWard.setAdministrativeUnit(foundAdmUnit);
            repo.update(wardNeedUpdateID, wardCode, ward.getName(), foundDistrict, foundAdmUnit);
            return mapper.map(repo.findById(wardCode), WardDto.class);
        }
        return null;
    }

    private boolean checkAdministrativeLv3(int admUnitId) {
        for (Utils.AdministrativeUnitsLv3 a : Utils.AdministrativeUnitsLv3.values()) {
            if (a.getValue() == admUnitId) {
                return true;
            }
        }
        return false;
    }
}
