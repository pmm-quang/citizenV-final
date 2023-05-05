package com.citizenv.app.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdministrativeDivisionDto {
    private String code;
    private String name;
    private AdministrativeUnitDto administrativeUnit;
}
