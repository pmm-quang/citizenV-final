package com.citizenv.app.service.impl;


import com.citizenv.app.entity.AdministrativeRegion;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.AdministrativeRegionDto;
import com.citizenv.app.repository.AdministrativeRegionRepository;
import com.citizenv.app.service.AdministrativeRegionService;
import com.citizenv.app.service.AdministrativeUnitService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdministrativeRegionServiceImpl implements AdministrativeRegionService {
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private AdministrativeRegionRepository repository;

    public List<AdministrativeRegionDto> getAll() {
        List<AdministrativeRegion> entities = repository.findAll();
        return entities.stream().map(l-> mapper.map(l, AdministrativeRegionDto.class)).collect(Collectors.toList());
    }

    public AdministrativeRegionDto getById(int admRegionId) {
        AdministrativeRegion ad = repository.findById(admRegionId).orElseThrow(
                () -> new ResourceNotFoundException("AdmRegion", "admRegionId", String.valueOf(admRegionId)));
        return mapper.map(ad, AdministrativeRegionDto.class);
    }
}
