package com.citizenv.app.service;

import com.citizenv.app.payload.CitizenDto;
import com.citizenv.app.payload.UserDto;
import com.citizenv.app.payload.custom.CustomCitizenRequest;
import com.citizenv.app.payload.custom.CustomCitizenResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface CitizenService {
    List<CitizenDto> getAll();
    CitizenDto getByNationalId(String nationalId);
    List<CitizenDto> getAllByHamletCode(String hamletCode);


    void deleteCitizen(String citizenId);

    Map<String, Object> getAllByHamletCode(String hamletCode, int page);

    Map<String, Object> getAllByWardCode(String wardCode, int page);

    Map<String, Object> getAllByDistrictCode(String districtCode, int page);


    String createCitizen(CustomCitizenRequest citizen);
    String updateCitizen(String nationalId, CustomCitizenRequest citizen);

    List<CustomCitizenResponse> searchCitizen(CustomCitizenRequest request);
}
