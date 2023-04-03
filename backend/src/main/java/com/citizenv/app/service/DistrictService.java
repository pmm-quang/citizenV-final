package com.citizenv.app.service;

import com.citizenv.app.entity.District;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.DistrictDto;
import com.citizenv.app.repository.DistrictRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DistrictService {
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private DistrictRepository repository;

    public List<DistrictDto> getAll() {
        List<District> entities = repository.findAll();
        return entities.stream().map(l-> mapper.map(l, DistrictDto.class)).collect(Collectors.toList());
    }

    public DistrictDto getById(String districtId) {
        District district = repository.findById(districtId).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "CustomerID", Long.parseLong(districtId)));
        return mapper.map(district, DistrictDto.class);
    }
}
