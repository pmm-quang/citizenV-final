package com.citizenv.app.payload.custom;

import lombok.Data;

@Data
public class CustomWardRequest {
    private String code;
    private String name;
    private String districtCode;
    private Integer administrativeUnitId;
}
