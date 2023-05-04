package com.citizenv.app.payload;

import lombok.Data;

@Data
public class ProvinceDto {
    private AdministrativeDivisionDto administrativeDivision;
    private AdministrativeRegionDto administrativeRegion;
    private String administrativeCode;
}

