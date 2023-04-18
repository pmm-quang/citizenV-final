package com.citizenv.app.service;

import com.citizenv.app.payload.WardDto;

import java.util.List;

public interface WardService {
    List<WardDto> getAll();
    WardDto getById(String wardId);

    List<WardDto> getByWardCode(String wardCode);
    WardDto createWard(String wardCode,String districtCode,WardDto ward);
    WardDto updateWard(String wardNeedUpdateID, WardDto ward);
}
