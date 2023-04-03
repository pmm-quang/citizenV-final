package com.citizenv.app.service;


import com.citizenv.app.entity.AdministrativeUnit;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.AdministrativeUnitDto;
import com.citizenv.app.repository.AdministrativeUnitRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdministrativeUnitService {
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private AdministrativeUnitRepository repository;

    public List<AdministrativeUnitDto> getAll() {
        List<AdministrativeUnit> entities = repository.findAll();
        return entities.stream().map(l-> mapper.map(l, AdministrativeUnitDto.class)).collect(Collectors.toList());
    }

    public AdministrativeUnitDto getById(String admUnitId) {
        AdministrativeUnit au = repository.findById(Integer.parseInt(admUnitId)).orElseThrow(
                () -> new ResourceNotFoundException("AdmUnit", "admUnitId", Long.parseLong(admUnitId)));
        return mapper.map(au, AdministrativeUnitDto.class);
    }
}
