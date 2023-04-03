package com.citizenv.app.payload;

import lombok.Data;

@Data
public class WardDto {
    private String code;
    private String name;
    private DistrictDto district;
    private AdministrativeUnitDto administrativeUnit;
}
