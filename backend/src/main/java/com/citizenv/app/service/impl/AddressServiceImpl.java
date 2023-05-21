package com.citizenv.app.service.impl;

import com.citizenv.app.entity.Address;
import com.citizenv.app.entity.AddressType;
import com.citizenv.app.entity.Citizen;
import com.citizenv.app.entity.Hamlet;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.AddressDto;
import com.citizenv.app.payload.AddressTypeDto;
import com.citizenv.app.repository.AddressRepository;
import com.citizenv.app.repository.AddressTypeRepository;
import com.citizenv.app.repository.CitizenRepository;
import com.citizenv.app.repository.HamletRepository;
import com.citizenv.app.service.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressTypeRepository addressTypeRepository;
    private final AddressRepository repository;
    private final CitizenRepository citizenRepository;
    private final HamletRepository hamletRepository;
    private final ModelMapper mapper;

    public AddressServiceImpl(AddressTypeRepository addressTypeRepository, AddressRepository repository, CitizenRepository citizenRepository, HamletRepository hamletRepository, ModelMapper mapper) {
        this.addressTypeRepository = addressTypeRepository;
        this.repository = repository;
        this.citizenRepository = citizenRepository;
        this.hamletRepository = hamletRepository;
        this.mapper = mapper;
    }

    @Override
    public List<AddressDto> getAll() {
        List<Address> list = repository.findAll();
        List<AddressDto> dtoList = list.stream().map(address -> mapper.map(address, AddressDto.class)).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public List<AddressDto> getByCitizenId(String citizenID) {
        Citizen foundCitizen = citizenRepository.findById(citizenID).orElseThrow(
                () -> new ResourceNotFoundException("Citizen", "CititzenID", citizenID)
        );
        List<Address> list = repository.findAllByCitizen(foundCitizen);
        List<AddressDto> dtoList = list.stream().map(address -> mapper.map(address, AddressDto.class)).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public List<AddressDto> getByHamletCode(String hamletCode) {
        Hamlet foundHamlet = hamletRepository.findByCode(hamletCode).orElseThrow(
                ()-> new ResourceNotFoundException("Hamlet", "HamletCode", hamletCode)
        );
        List<Address> list = repository.findAllByHamlet(foundHamlet);
        List<AddressDto> dtoList = list.stream().map(address -> mapper.map(address, AddressDto.class)).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public List<AddressDto> getByHamletCodeAndAddressType(String hamletCode, Integer addressTypeId) {
        Hamlet foundHamlet = hamletRepository.findByCode(hamletCode).orElseThrow(
                ()-> new ResourceNotFoundException("Hamlet", "HamletCode", hamletCode)
        );

        AddressType foundAddressType = addressTypeRepository.findById(addressTypeId).orElseThrow(
                ()-> new ResourceNotFoundException("AddressType", "AddressTypeID", addressTypeId.toString())
        );
        List<Address> list = repository.findAllByHamletAndAddressType(foundHamlet, foundAddressType);
        List<AddressDto> dtoList = list.stream().map(address -> mapper.map(address, AddressDto.class)).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public List<AddressTypeDto> getAllAddressType() {
        List<AddressType> list = addressTypeRepository.findAll();
        List<AddressTypeDto> dtoList = list.stream().map(addressType -> mapper.map(addressType, AddressTypeDto.class)).collect(Collectors.toList());
        return dtoList;
    }
}
