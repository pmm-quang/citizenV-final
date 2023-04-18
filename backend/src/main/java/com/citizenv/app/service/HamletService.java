package com.citizenv.app.service;

import com.citizenv.app.payload.HamletDto;

import java.util.List;

public interface HamletService {
    List<HamletDto> getAll();
    HamletDto getByCode(String hamletCode);

    List<HamletDto> getAllByWardCode(String wardCode);

    List<HamletDto> getAllByAdministrativeUnitId(int administrativeUnitID);
    HamletDto createHamlet(HamletDto hamlet);
    HamletDto updateHamlet(String hamletCode, HamletDto hamlet);
}
