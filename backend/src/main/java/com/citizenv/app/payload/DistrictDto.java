package com.citizenv.app.payload;

import lombok.Data;

@Data
public class DistrictDto {
    private String code;
    private String name;
    private AdministrativeUnitDto administrativeUnit;
    private ProvinceDto province;
}
