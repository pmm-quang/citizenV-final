package com.citizenv.app.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProvinceDto extends AdministrativeDivisionDto {
    private AdministrativeRegionDto administrativeRegion;
    private String administrativeCode;
}

