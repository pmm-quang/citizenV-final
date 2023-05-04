package com.citizenv.app.service;

import com.citizenv.app.payload.AdministrativeRegionDto;

import java.util.List;

public interface AdministrativeRegionService {
    List<AdministrativeRegionDto> getAll();
    AdministrativeRegionDto getById(int admRegionId);
}
