package com.citizenv.app.service;

import com.citizenv.app.entity.Declaration;
import com.citizenv.app.payload.DeclarationDto;
import com.citizenv.app.repository.DeclarationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeclarationService {
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private DeclarationRepository repository;

    public List<DeclarationDto> getAll() {
        List<Declaration> entities = repository.findAll();
        return entities.stream().map(l-> mapper.map(l, DeclarationDto.class)).collect(Collectors.toList());
    }
}
