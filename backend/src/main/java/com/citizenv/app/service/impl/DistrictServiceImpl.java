package com.citizenv.app.service.impl;

import com.citizenv.app.component.Constant;
import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.AdministrativeUnit;
import com.citizenv.app.entity.District;
import com.citizenv.app.entity.Province;
import com.citizenv.app.exception.InvalidException;
import com.citizenv.app.exception.ResourceFoundException;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.DistrictDto;
import com.citizenv.app.repository.AdministrativeDivisionRepository;
import com.citizenv.app.repository.AdministrativeUnitRepository;
import com.citizenv.app.repository.DistrictRepository;
import com.citizenv.app.repository.ProvinceRepository;
import com.citizenv.app.service.DistrictService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DistrictServiceImpl implements DistrictService {
    private final ModelMapper mapper;

    private final DistrictRepository repository;

    private final ProvinceRepository provinceRepository;

    private final AdministrativeUnitRepository administrativeUnitRepository;

    private final AdministrativeDivisionRepository administrativeDivisionRepository;

    public DistrictServiceImpl(ModelMapper mapper, DistrictRepository repository, ProvinceRepository provinceRepository, AdministrativeUnitRepository administrativeUnitRepository, AdministrativeDivisionRepository administrativeDivisionRepository) {
        this.mapper = mapper;
        this.repository = repository;
        this.provinceRepository = provinceRepository;
        this.administrativeUnitRepository = administrativeUnitRepository;
        this.administrativeDivisionRepository = administrativeDivisionRepository;
    }

    @Override
    public List<DistrictDto> getAll() {
        List<District> entities = repository.findAll();
        return entities.stream().map(l-> mapper.map(l, DistrictDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<DistrictDto> getAll(String divisionCodeOfUserDetail) {
        List<District> list = new ArrayList<>();
        if (divisionCodeOfUserDetail == null) {
            list.addAll(repository.findAll());
        } else {
            list.addAll(repository.findAllBySupDivisionCode(divisionCodeOfUserDetail));
        }
        return list.stream().map(district -> mapper.map(district, DistrictDto.class)).collect(Collectors.toList());
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
                () -> new ResourceNotFoundException("Quận/huyện/thị xã", "mã định danh", code)
        );
        return mapper.map(district, DistrictDto.class);
    }

    @Override
    public List<DistrictDto> getAllByProvinceCode(String provinceCode) {
        Province foundProvince = provinceRepository.findByCode(provinceCode).orElseThrow(
                () -> new ResourceNotFoundException("Tỉnh/thành phố", "mã định danh", provinceCode)
        );
        List<District> list = repository.findAllByProvince(foundProvince);
        List<DistrictDto> dtoList = list.stream().map(district -> mapper.map(district, DistrictDto.class)).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public List<DistrictDto> getAllByAdministrativeUnitId(int admUnitId) {
        AdministrativeUnit foundAdmUnit = administrativeUnitRepository.findById(admUnitId).orElseThrow(
                ()-> new ResourceNotFoundException("Đơn vị hành chính", "id", String.valueOf(admUnitId))
        );
        List<District> list = repository.findAllByAdministrativeUnit(foundAdmUnit);
        List<DistrictDto> dtoList = list.stream().map(district -> mapper.map(district, DistrictDto.class)).collect(Collectors.toList());
        return dtoList;
    }


    @Transactional
    @Override
    public String createDistrict(String divisionCodeOfUserDetail, DistrictDto district) {
        String newDistrictCode = district.getCode();
        repository.findByCode(newDistrictCode).ifPresent(
                foundDistrict -> {throw new ResourceFoundException("Quận/huyện/thị xã", "mã định danh", newDistrictCode);});

        administrativeDivisionRepository.findByName(district.getName(), divisionCodeOfUserDetail).ifPresent(
                division -> {throw new InvalidException(Constant.ERR_MESSAGE_UNIT_NAME_ALREADY_EXISTS);}
        );
        Map<String, Object> map = validate(district);
        Province foundProvince = (Province) map.get("province");
        AdministrativeUnit foundAdmUnit = (AdministrativeUnit) map.get("admUnit");
        District createDistrict = mapper.map(district, District.class);
        createDistrict.setProvince(foundProvince);
        createDistrict.setAdministrativeUnit(foundAdmUnit);
        District newDistrict = repository.save(createDistrict);
        return "Tạo mới quận/huyện/thị xã thành công!";
    }

    @Transactional
    @Override
    public String updateDistrict(String districtCodeNeedUpdate, DistrictDto district) {
        String districtCode = district.getCode();
        District foundDistrict = repository.findByCode(districtCodeNeedUpdate).orElseThrow(
                () -> new ResourceNotFoundException("Quận/huyện/thị xã", "mã định danh", districtCodeNeedUpdate)
        );

        if (!districtCode.equals(districtCodeNeedUpdate)) {
            repository.findByCode(districtCode).ifPresent(
                    fd -> {throw new ResourceFoundException("Quận/huyện/thị xã", "mã định danh", districtCode);}
            );
        }

        if (!foundDistrict.getName().equals(district.getCode())) {
            administrativeDivisionRepository.findByName(district.getName(), district.getProvince().getCode()).ifPresent(
                    p -> {throw new InvalidException(Constant.ERR_MESSAGE_UNIT_NAME_ALREADY_EXISTS);}
            );
        }

        Map<String, Object> map = validate(district);
        Province foundProvince = (Province) map.get("province");
        AdministrativeUnit foundAdmUnit = (AdministrativeUnit) map.get("admUnit");
        foundDistrict.setCode(district.getCode());
        foundDistrict.setName(district.getName());
        foundDistrict.setProvince(foundProvince);
        foundDistrict.setAdministrativeUnit(foundAdmUnit);
        if (!districtCode.equals(districtCodeNeedUpdate)) {
            administrativeDivisionRepository.updateCodeOfSubDivision(district.getCode(), 5, districtCodeNeedUpdate);
        }
        return "Chỉnh sửa quận/huyện/thị xã thành công!";
    }
    @Override
    public void deleteDistrict(String districtCode) {

    }


    private Map<String, Object> validate(DistrictDto district) {
        Integer admUnitId = district.getAdministrativeUnit().getId();
        String districtCode = district.getCode();
        String provinceCode = district.getProvince().getCode();
        boolean checkNull = (districtCode == null || districtCode.equals("") || district.getName() == null
                || district.getName().equals(""));
        if (checkNull) {
            throw new InvalidException(Constant.ERR_MESSAGE_NOT_ENTERED_THE_REQUIRED_INFO);
        }
        if (!Utils.AdministrativeUnitsLv2.containsKey(admUnitId)) {
            throw new InvalidException(Constant.ERR_MESSAGE_ADMINISTRATIVE_UNIT_INVALID);
        }
        if (districtCode.indexOf(provinceCode) != 0) {
            throw new InvalidException(Constant.ERR_MESSAGE_UNIT_CODE_INVALID);
        }
//        if (!Utils.validateName(name)) {
//            throw new InvalidException("Ten khong hop le");
//        }
        if (!Utils.validateFormatDivisionCode(districtCode)) {
            throw new InvalidException(Constant.ERR_MESSAGE_UNIT_CODE_INVALID);
        }
//        String provinceCode = district.getProvince().getCode();
        Province foundProvince = provinceRepository.findByCode(provinceCode).orElseThrow(
                ()-> new ResourceNotFoundException("Tỉnh/thành phố", "mã định danh", provinceCode)
        );

//        int admUnitId = district.getAdministrativeUnit().getId();
        AdministrativeUnit foundAdmUnit = administrativeUnitRepository.findById(admUnitId).orElseThrow(
                () -> new ResourceNotFoundException("Đơn vị hành chính", "id", String.valueOf(admUnitId))
        );
        Map<String, Object> map = new HashMap<>();
        map.put("province", foundProvince);
        map.put("admUnit", foundAdmUnit);
        return map;
    }
}
