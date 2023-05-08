package com.citizenv.app.service;

import com.citizenv.app.payload.CitizenDto;

import java.util.List;

public interface CitizenService {
    List<CitizenDto> getAll();
    CitizenDto getById(String citizenId);
    List<CitizenDto> getAllByHamletCode(String hamletCode);
    List<CitizenDto> getAllByWardCode(String wardCode);
    List<CitizenDto> getAllByDistrictCode(String districtCode);
    List<CitizenDto> getAllByProvinceCode(String provinceCode);
    List<CitizenDto> getAllByAddressId(String addressId); //get dia chi thuong tru
    CitizenDto createCitizen(CitizenDto citizen);
    CitizenDto updateCitizen(String citizenId, CitizenDto citizen);

    CitizenDto deleteCitizen(String citizenId);

}
