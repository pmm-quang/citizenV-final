package com.citizenv.app.payload.population;

import com.citizenv.app.payload.AddressTypeDto;
import com.citizenv.app.payload.HamletDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class DivisionGeneralPopulationDto extends PopulationDto {
    private String code;

    public DivisionGeneralPopulationDto() {
        super();
    }

    public DivisionGeneralPopulationDto(String code, String name) {
        super(name);
        this.code = code;
    }

    public DivisionGeneralPopulationDto(String code, String name, Long init) {
        super(name, init);
        this.code = code;
    }
}
