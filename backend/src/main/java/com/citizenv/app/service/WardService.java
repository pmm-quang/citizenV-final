package com.citizenv.app.service;

import com.citizenv.app.payload.WardDto;

import java.util.List;

public interface WardService {
    List<WardDto> getAll();
    WardDto getById(String wardId);

    List<WardDto> getByDistrictCode(String wardCode);

    List<WardDto> getByAdministrativeUnitId(int admId);
    WardDto createWard(String wardCode,String districtCode,WardDto ward);

    WardDto createWard(WardDto ward);
    WardDto updateWard(String wardNeedUpdateID, WardDto ward);
}
