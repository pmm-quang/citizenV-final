package com.citizenv.app.service;

import com.citizenv.app.payload.ProvinceDto;

import java.util.List;
import java.util.Map;

public interface ProvinceService {
    List<ProvinceDto> getAll();

    Map<String, Object> getAll(int page);
    ProvinceDto getById(Long provinceId);
    ProvinceDto getByCode(String code);
    List<ProvinceDto> getAllByAdministrativeUnitId(int admUnitId);
    List<ProvinceDto> getAllByAdministrativeRegionId(int admRegionId);
    ProvinceDto createProvince(ProvinceDto province);
    ProvinceDto updateProvince(String provinceCodeNeedUpdate, ProvinceDto province);
    String deleteById(Long provinceCode);
}
