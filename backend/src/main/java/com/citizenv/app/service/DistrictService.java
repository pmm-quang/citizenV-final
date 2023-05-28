package com.citizenv.app.service;

import com.citizenv.app.payload.DistrictDto;

import java.util.List;
import java.util.Map;

public interface DistrictService {
    List<DistrictDto> getAll();

    List<DistrictDto> getAll(String divisionCodeOfUserDetail);
    DistrictDto getById(Long districtId);

    DistrictDto getByCode(String code);

    List<DistrictDto> getAllByProvinceCode(String provinceCode);

    List<DistrictDto> getAllByAdministrativeUnitId(int admUnitId);

    String createDistrict(String divisionCodeOfUserDetail, DistrictDto district);

    String updateDistrict(String districtCodeNeedUpdate, DistrictDto district);

    void deleteDistrict(String districtCode);


}
