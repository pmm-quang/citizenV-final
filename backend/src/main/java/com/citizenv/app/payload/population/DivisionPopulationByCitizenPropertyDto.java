package com.citizenv.app.payload.population;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DivisionPopulationByCitizenPropertyDto {
    private String code;
    private String name;
    private String property;
    private List<PopulationDto> details;

    public DivisionPopulationByCitizenPropertyDto(String code, String name, String property, ArrayList<PopulationDto> populationDtos) {
        this.code = code;
        this.name = name;
        this.property = property;
        details = populationDtos;
    }
}
