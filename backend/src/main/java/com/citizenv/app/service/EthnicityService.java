package com.citizenv.app.service;

import com.citizenv.app.payload.EthnicityDto;
import com.citizenv.app.payload.HamletDto;

import java.util.List;

public interface EthnicityService {
    List<EthnicityDto> getAll();
}