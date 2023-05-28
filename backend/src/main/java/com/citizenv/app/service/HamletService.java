package com.citizenv.app.service;

import com.citizenv.app.payload.HamletDto;
import com.citizenv.app.payload.custom.CustomHamletRequest;

import java.util.List;

public interface HamletService {
    List<HamletDto> getAll();
    HamletDto getByCode(String hamletCode);

    List<HamletDto> getAllByWardCode(String wardCode);

    List<HamletDto> getAllByAdministrativeUnitId(int administrativeUnitID);
    String createHamlet(String divisionCode, CustomHamletRequest hamlet);

    String updateHamlet(String hamletCodeNeedUpdate, CustomHamletRequest hamlet);
    void nono();

    List<HamletDto> getAll(String divisionCodeOfUserDetail);
}
