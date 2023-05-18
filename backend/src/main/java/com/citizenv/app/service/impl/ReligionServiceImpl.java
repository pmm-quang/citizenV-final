package com.citizenv.app.service.impl;

import com.citizenv.app.entity.Religion;
import com.citizenv.app.payload.EthnicityDto;
import com.citizenv.app.payload.ReligionDto;
import com.citizenv.app.repository.ReligionRepository;
import com.citizenv.app.service.ReligionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReligionServiceImpl implements ReligionService {

    final
    ReligionRepository repository;

    final
    ModelMapper mapper;

    public ReligionServiceImpl(ReligionRepository religionRepository, ModelMapper mapper) {
        this.repository = religionRepository;
        this.mapper = mapper;
    }

    @Override
    public List<ReligionDto> getAll() {
        List<Religion> entities = repository.findAll();
        return entities.stream().map(l-> mapper.map(l, ReligionDto.class)).collect(Collectors.toList());
    }
}
