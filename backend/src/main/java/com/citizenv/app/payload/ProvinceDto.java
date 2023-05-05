package com.citizenv.app.payload;

import lombok.Data;

@Data
public class ProvinceDto {
    private String code;
    private String name;
    private AdministrativeUnitDto administrativeUnit;
    private AdministrativeRegionDto administrativeRegion;
    private String administrativeCode;
}

