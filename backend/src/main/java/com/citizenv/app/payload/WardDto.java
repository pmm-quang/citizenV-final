package com.citizenv.app.payload;

import lombok.Data;

@Data
public class WardDto {
    private String code;
    private String name;
    private AdministrativeUnitDto administrativeUnit;
    private DistrictDto district;
}
