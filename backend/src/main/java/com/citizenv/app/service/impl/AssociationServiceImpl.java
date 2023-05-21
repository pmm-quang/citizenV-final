package com.citizenv.app.service.impl;

import com.citizenv.app.entity.Association;
import com.citizenv.app.entity.AssociationType;
import com.citizenv.app.payload.AssociationDto;
import com.citizenv.app.payload.AssociationTypeDto;
import com.citizenv.app.repository.AssociationRepository;
import com.citizenv.app.repository.AssociationTypeRepository;
import com.citizenv.app.service.AssociationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssociationServiceImpl implements AssociationService {
    private final AssociationRepository associationRepository;
    private final AssociationTypeRepository associationTypeRepository;
    private final ModelMapper mapper;

    public AssociationServiceImpl(AssociationRepository associationRepository, AssociationTypeRepository associationTypeRepository, ModelMapper mapper) {
        this.associationRepository = associationRepository;
        this.associationTypeRepository = associationTypeRepository;
        this.mapper = mapper;
    }

    @Override
    public List<AssociationDto> getAllAssociation() {
        List<Association> list = associationRepository.findAll();
        List<AssociationDto> dtos = list.stream().map(
                association -> mapper.map(association, AssociationDto.class)).collect(Collectors.toList());
        return dtos;
    }

    @Override
    public List<AssociationTypeDto> getAllAssociationType() {
        List<AssociationType> list = associationTypeRepository.findAll();
        List<AssociationTypeDto> dtos = list.stream().map(
                association -> mapper.map(association, AssociationTypeDto.class)).collect(Collectors.toList());
        return dtos;
    }
}
