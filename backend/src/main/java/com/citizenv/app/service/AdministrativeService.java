package com.citizenv.app.service;

import com.citizenv.app.payload.AdministrativeRegionDto;
import com.citizenv.app.payload.AdministrativeUnitDto;

import java.util.List;

public interface AdministrativeService {
    List<AdministrativeRegionDto> getAllAdministrativeRegion();
    AdministrativeRegionDto getAdministrativeRegionById(int admRegionId);
    List<AdministrativeUnitDto> getAllAdministrativeUnit();
    AdministrativeUnitDto getAdministrativeUnitById(int admUnitId);
}
