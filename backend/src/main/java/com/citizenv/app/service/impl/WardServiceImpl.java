package com.citizenv.app.service.impl;

import com.citizenv.app.component.Constant;
import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.AdministrativeDivision;
import com.citizenv.app.entity.AdministrativeUnit;
import com.citizenv.app.entity.District;
import com.citizenv.app.entity.Ward;
import com.citizenv.app.exception.InvalidException;
import com.citizenv.app.exception.ResourceFoundException;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.WardDto;
import com.citizenv.app.payload.custom.CustomWardRequest;
import com.citizenv.app.repository.AdministrativeDivisionRepository;
import com.citizenv.app.repository.AdministrativeUnitRepository;
import com.citizenv.app.repository.DistrictRepository;
import com.citizenv.app.repository.WardRepository;
import com.citizenv.app.secirity.CustomUserDetail;
import com.citizenv.app.service.WardService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class WardServiceImpl implements WardService {
    private final ModelMapper mapper;
    private final WardRepository repo;
    private final DistrictRepository districtRepo;
    private final AdministrativeUnitRepository admUnitRepo;
    private final AdministrativeDivisionRepository admDivisionRepo;

    public WardServiceImpl(ModelMapper mapper, WardRepository repo, DistrictRepository districtRepo, AdministrativeUnitRepository admUnitRepo,
                           AdministrativeDivisionRepository admDivisionRepo) {
        this.mapper = mapper;
        this.repo = repo;
        this.districtRepo = districtRepo;
        this.admUnitRepo = admUnitRepo;
        this.admDivisionRepo = admDivisionRepo;
    }

    @Override
    public List<WardDto> getAll() {
        List<Ward> entities = repo.findAll();
        return entities.stream().map(l-> mapper.map(l, WardDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<WardDto> getAll(String divisionCodeOfUserDetail) {
        List<Ward> entities = new ArrayList<>();
        if (divisionCodeOfUserDetail == null) {
            entities.addAll(repo.findAll());
        } else {
            entities.addAll(repo.findAllBySupDivisionCode(divisionCodeOfUserDetail));
        }
        return entities.stream().map(l-> mapper.map(l, WardDto.class)).collect(Collectors.toList());
    }

    @Override
    public WardDto getByCode(String code) {
        Ward foundWard = repo.findByCode(code).orElseThrow(
                () -> new ResourceNotFoundException("Xã/phường/thị trấn", "mã đơn vị" , code)
        );
        return mapper.map(foundWard, WardDto.class);
    }

    @Override
    public List<WardDto> getByDistrictCode(String districtCode) {
        District foundDistrict = districtRepo.findByCode(districtCode).orElseThrow(
                () -> new ResourceNotFoundException("Quận/huyện/thị xã", "mã đơn vị", districtCode)
        );
        List<Ward> list = repo.findAllByDistrict(foundDistrict);
        List<WardDto> dtoList = list.stream().map(ward -> mapper.map(ward, WardDto.class)).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public List<WardDto> getByAdministrativeUnitId(int admId) {
        AdministrativeUnit foundAdmUnit = admUnitRepo.findById(admId).orElseThrow(
                () -> new ResourceNotFoundException("Đơn vị hành chính", "id", String.valueOf(admId))
        );
        List<Ward> list = repo.findAllByAdministrativeUnit(foundAdmUnit);
        return list.stream().map(ward -> mapper.map(ward, WardDto.class)).collect(Collectors.toList());
    }

    @Override
    public String createWard(String divisionCode, CustomWardRequest ward) {
        repo.findByCode(ward.getCode()).ifPresent(w -> {
            throw new ResourceFoundException("Xã/phường/thị trấn", "mã đơn vị", ward.getCode());
        });
        admDivisionRepo.findByName(ward.getName(), divisionCode).ifPresent(
                division -> {throw new InvalidException(Constant.ERR_MESSAGE_UNIT_NAME_ALREADY_EXISTS);}
        );
        Ward createWard = validate(ward);
        Ward newWard = repo.save(createWard);
        return "Created success!";
    }
    @Transactional
    @Override
    public String updateWard(String wardNeedUpdateCode, CustomWardRequest ward) {

        Ward foundWard = repo.findByCode(wardNeedUpdateCode).orElseThrow(
                () -> new ResourceNotFoundException("Xã/phường/thị trấn", "mã đơn vị", wardNeedUpdateCode)
        );
        if (ward.getCode()!= null && !ward.getCode().equals(wardNeedUpdateCode)) {
            repo.findByCode(ward.getCode()).ifPresent(
                    w -> {throw new ResourceFoundException("Xã/phường/thị trấn", "mã đơn vị", ward.getCode());}
            );
        }

        Ward createWard = validate(ward);

        if (!foundWard.getName().equals(ward.getName())) {
            String districtCode = ward.getCode().substring(0,6);
            admDivisionRepo.findByName(ward.getName(), districtCode).ifPresent(
                    division -> {throw new InvalidException(Constant.ERR_MESSAGE_UNIT_NAME_ALREADY_EXISTS);}
            );
        }
        foundWard.setCode(createWard.getCode());
        foundWard.setName(createWard.getName());
        foundWard.setAdministrativeUnit(createWard.getAdministrativeUnit());
        foundWard.setDistrict(createWard.getDistrict());

        if (!ward.getCode().equals(wardNeedUpdateCode)) {
            admDivisionRepo.updateCodeOfSubDivision(ward.getCode(), 7, wardNeedUpdateCode);
        }
        return "Updated success!";
    }

    private Ward validate(CustomWardRequest ward) {
        String wardCode = ward.getCode();
        String wardName = ward.getName();
        String districtCode = ward.getDistrictCode();
        boolean checkNullRequiredInfo = (ward.getCode() == null || ward.getCode().equals("")
                || ward.getName() == null || ward.getName().equals("")
                || ward.getDistrictCode() == null || ward.getDistrictCode().equals("")
                || ward.getAdministrativeUnitId() == null);
        if (checkNullRequiredInfo) {
            throw new InvalidException(Constant.ERR_MESSAGE_NOT_ENTERED_THE_REQUIRED_INFO);
        }
        Integer admUnitId = ward.getAdministrativeUnitId();
        AdministrativeUnit foundAdmUnit = admUnitRepo.findById(admUnitId).orElseThrow(
                () -> new ResourceNotFoundException("Đơn vị hành chính","id", String.valueOf(admUnitId))
        );

        District foundDistrict = districtRepo.findByCode(districtCode).orElseThrow(
                () -> new ResourceNotFoundException("Quận/huyện/thị xã", "mã đơn vị", districtCode)
        );

        if (!Utils.AdministrativeUnitsLv3.containsKey(admUnitId)) {
            throw new InvalidException(Constant.ERR_MESSAGE_ADMINISTRATIVE_UNIT_INVALID);
        }
        if (wardCode.indexOf(districtCode) != 0 || !Utils.validateFormatDivisionCode(wardCode)) {
            throw new InvalidException(Constant.ERR_MESSAGE_UNIT_CODE_INVALID);
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
