package com.citizenv.app.service.impl;

import com.citizenv.app.entity.Ethnicity;
import com.citizenv.app.entity.Province;
import com.citizenv.app.payload.EthnicityDto;
import com.citizenv.app.payload.ProvinceDto;
import com.citizenv.app.repository.EthnicityRepository;
import com.citizenv.app.service.EthnicityService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EthnicityServiceImpl implements EthnicityService {
    private final ModelMapper mapper;
    private final EthnicityRepository repository;

    public EthnicityServiceImpl(EthnicityRepository ethnicityRepository, ModelMapper mapper) {
        this.repository = ethnicityRepository;
        this.mapper = mapper;
    }

    @Override
    public List<EthnicityDto> getAll() {
        List<Ethnicity> provinceEntities = repository.findAll();
        return provinceEntities.stream().map(l-> mapper.map(l, EthnicityDto.class)).collect(Collectors.toList());
    }
}
