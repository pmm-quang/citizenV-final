package com.citizenv.app.service;

import com.citizenv.app.payload.AddressDto;
import com.citizenv.app.payload.AddressTypeDto;

import java.util.List;

public interface AddressService {
    List<AddressDto> getAll();
    List<AddressDto> getByCitizenId(String citizenID);
    List<AddressDto> getByHamletCode(String hamletCode);
    List<AddressDto> getByHamletCodeAndAddressType(String hamletCode, String addressTypeId);
    List<AddressTypeDto> getAllAddressType();

}
