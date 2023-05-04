package com.citizenv.app.service;

import com.citizenv.app.payload.DistrictDto;

import java.util.List;
import java.util.Map;

public interface DistrictService {
    List<DistrictDto> getAll();
    DistrictDto getById(String districtId);

    List<DistrictDto> getAllByProvinceCode(String provinceCode);

    List<DistrictDto> getAllByAdministrativeUnitId(int admUnitId);

    DistrictDto createDistrict(Map<String, Object> JSONInfoAsMap);

    DistrictDto createDistrict(DistrictDto district);

    DistrictDto updateDistrict(String districtCodeNeedUpdate, DistrictDto district);
    DistrictDto updateDistrict(String districtIdNeedUpdate, Map<String, Object> JSONInfoAsMap);

}
