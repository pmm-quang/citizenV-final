package com.citizenv.app.service;

import com.citizenv.app.payload.WardDto;
import com.citizenv.app.payload.custom.CustomWardRequest;
import com.citizenv.app.secirity.CustomUserDetail;

import java.util.List;

public interface WardService {
    List<WardDto> getAll();

    List<WardDto> getAll(CustomUserDetail userDetail);
    WardDto getByCode(String code);
    List<WardDto> getByDistrictCode(String wardCode);

    List<WardDto> getByAdministrativeUnitId(int admId);

    WardDto createWard(String divisionCode, CustomWardRequest ward);

    WardDto updateWard(String wardNeedUpdateCode, CustomWardRequest ward);
}
