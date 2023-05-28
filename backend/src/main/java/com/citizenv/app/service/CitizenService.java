package com.citizenv.app.service;

import com.citizenv.app.payload.CitizenDto;
import com.citizenv.app.payload.UserDto;
import com.citizenv.app.payload.custom.CustomCitizenRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface CitizenService {
    List<CitizenDto> getAll();

    Map<String, Object> getAll(int page);
    CitizenDto getById(String citizenId);
    CitizenDto getByNationalId(String nationalId);
    List<CitizenDto> getAllByHamletCode(String hamletCode);
    List<CitizenDto> getAllByWardCode(String wardCode);
    List<CitizenDto> getAllByDistrictCode(String districtCode);
    List<CitizenDto> getAllByProvinceCode(String provinceCode);
    List<CitizenDto> getAllByAddressId(String addressId); //get dia chi thuong tru

    void deleteCitizen(String citizenId);

    Map<String, Object> getAllByHamletCode(String hamletCode, int page);

    Map<String, Object> getAllByWardCode(String wardCode, int page);

    Map<String, Object> getAllByDistrictCode(String districtCode, int page);

    Map<String, Object> getAllByProvinceCode(String provinceCode, int page);

    String createCitizen(CustomCitizenRequest citizen);
    String updateCitizen(String nationalId, CustomCitizenRequest citizen);
    String createUserFromExcelFile(MultipartFile excelFile);
}
