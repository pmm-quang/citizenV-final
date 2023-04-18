package com.citizenv.app.service.impl;

import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.District;
import com.citizenv.app.entity.Ward;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.WardDto;
import com.citizenv.app.repository.AdministrativeUnitRepository;
import com.citizenv.app.repository.DistrictRepository;
import com.citizenv.app.repository.WardRepository;
import com.citizenv.app.service.WardService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WardServiceImpl implements WardService {
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private WardRepository repository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private AdministrativeUnitRepository administrativeUnitRepository;

    @Override
    public List<WardDto> getAll() {
        List<Ward> entities = repository.findAll();
        return entities.stream().map(l-> mapper.map(l, WardDto.class)).collect(Collectors.toList());
    }

    @Override
    public WardDto getById(String wardCode) {
       Ward foundWard = repository.findById(wardCode)
               .orElseThrow(() -> new ResourceNotFoundException("Ward", "WardCode",wardCode));
        return mapper.map(foundWard, WardDto.class);
    }

    @Override
    public List<WardDto> getByWardCode(String wardCode) {
        return null;
    }

    @Override
    public WardDto createWard(String wardCode,String districtCode, WardDto ward) {
        Optional<Ward> foundWard = repository.findById(wardCode);
        if (foundWard.isEmpty()) {
            int administrativeUnitsID = ward.getAdministrativeUnit().getId();
            boolean isAdministrativeUnitsLv3 = false;
            for (Utils.AdministrativeUnitsLv3 a : Utils.AdministrativeUnitsLv3.values()) {
                if (a.getId() == administrativeUnitsID) {
                    isAdministrativeUnitsLv3 = true;
                    break;
                }
            }
            if (!isAdministrativeUnitsLv3) {
                return null;
            }

            District foundDistrict = districtRepository.findById(districtCode)
                    .orElseThrow(() -> new ResourceNotFoundException("District", "DistrictCode", districtCode));
            Ward newWard = mapper.map(ward, Ward.class);
            repository.save(newWard);
            return mapper.map(newWard, WardDto.class);
        }
        return null;
    }

    @Override
    public WardDto updateWard(String wardNeedUpdateID, WardDto ward) {
        Ward foundWard = repository.findById(wardNeedUpdateID).orElseThrow(
                () -> new ResourceNotFoundException("Ward", "WardCode", wardNeedUpdateID)
        );
        int administrativeUnitsID = ward.getAdministrativeUnit().getId();
        boolean isAdministrativeUnitsLv3 = false;
        for (Utils.AdministrativeUnitsLv3 a : Utils.AdministrativeUnitsLv3.values()) {
            if (a.getId() == administrativeUnitsID) {
                isAdministrativeUnitsLv3 = true;
                break;
            }
        }
        if (isAdministrativeUnitsLv3) {
            districtRepository.findById(ward.getDistrict().getCode()).ifPresent(foundDistrict -> {
                administrativeUnitRepository.findById(administrativeUnitsID).ifPresent(foundAdmUnit -> {
                    foundWard.setName(ward.getName());
                    foundWard.setDistrict(foundDistrict);
                    foundWard.setAdministrativeUnit(foundAdmUnit);
                });
            });
            repository.save(foundWard);
            return mapper.map(foundWard, WardDto.class);
        }
        return null;
    }
}
