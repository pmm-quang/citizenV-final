package com.citizenv.app.payload.population;

import com.citizenv.app.payload.AddressTypeDto;
import com.citizenv.app.payload.HamletDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class DivisionGeneralPopulationDto {
    private String code;
    private String name;
    private Long population;

    public DivisionGeneralPopulationDto(String code, String name) {
        this.code = code;
        this.name = name;
        population = 1L;
    }

    public void increasePopulation(Long count) {
        population += count;
    }
}
