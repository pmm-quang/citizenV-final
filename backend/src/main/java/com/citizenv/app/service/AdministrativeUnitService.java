package com.citizenv.app.service;

import com.citizenv.app.payload.AdministrativeUnitDto;

import java.util.List;

public interface AdministrativeUnitService {
    List<AdministrativeUnitDto> getAll();
    AdministrativeUnitDto getById(int admUnitId);
}
