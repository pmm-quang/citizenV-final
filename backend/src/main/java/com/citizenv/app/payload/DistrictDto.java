package com.citizenv.app.payload;

import lombok.Data;

@Data
public class DistrictDto {
    private AdministrativeDivisionDto administrativeDivision;
    private ProvinceDto province;
}
