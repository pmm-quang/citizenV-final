package com.citizenv.app.service;

import com.citizenv.app.payload.ProvinceDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface ProvinceService {
    List<ProvinceDto> getAll();
    ProvinceDto getById(String provinceId);
    List<ProvinceDto> getAllByAdministrativeUnitId(int admUnitId);
    List<ProvinceDto> getAllByAdministrativeRegionId(int admRegionId);
    ProvinceDto createProvince(Map<String, Object> JSONInfoAsMap);
    ProvinceDto updateProvince(String provinceIdNeedUpdate, ProvinceDto province);
    String deleteById(String provinceId);
}
