package com.citizenv.app.service;

import com.citizenv.app.payload.WardDto;
import com.citizenv.app.payload.custom.CustomWardRequest;
import com.citizenv.app.secirity.CustomUserDetail;

import java.util.List;

public interface WardService {
    List<WardDto> getAll();
    List<WardDto> getAll(String divisionCodeOfUserDetail);
    WardDto getByCode(String code);
    List<WardDto> getByDistrictCode(String wardCode);

    List<WardDto> getByAdministrativeUnitId(int admId);

    String createWard(String divisionCode, CustomWardRequest ward);

    String updateWard(String wardNeedUpdateCode, CustomWardRequest ward);
}
