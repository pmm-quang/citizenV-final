package com.citizenv.app.service.impl;


import com.citizenv.app.entity.AdministrativeUnit;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.AdministrativeUnitDto;
import com.citizenv.app.repository.AdministrativeUnitRepository;
import com.citizenv.app.service.AdministrativeUnitService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdministrativeUnitServiceImpl implements AdministrativeUnitService {
    private final ModelMapper mapper;

    private final AdministrativeUnitRepository repository;

    public AdministrativeUnitServiceImpl(ModelMapper mapper, AdministrativeUnitRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public List<AdministrativeUnitDto> getAll() {
        List<AdministrativeUnit> entities = repository.findAll();
        return entities.stream().map(l-> mapper.map(l, AdministrativeUnitDto.class)).collect(Collectors.toList());
    }

    public AdministrativeUnitDto getById(int admUnitId) {
        AdministrativeUnit au = repository.findById(admUnitId).orElseThrow(
                () -> new ResourceNotFoundException("AdmUnit", "admUnitId", String.valueOf(admUnitId)));
        return mapper.map(au, AdministrativeUnitDto.class);
    }
}
