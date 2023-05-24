package com.citizenv.app.payload.population;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class DivisionPopulationByCitizenPropertyDto extends DivisionGeneralPopulationDto {
    private List<PopulationDto> details;

    public DivisionPopulationByCitizenPropertyDto(List<PopulationDto> populationDtos) {
        details = populationDtos;
    }

    public DivisionPopulationByCitizenPropertyDto(String c, String n, List<PopulationDto> populationDtos) {
        super(c, n);
        details = populationDtos;
    }
    public void recalculatePopulation() {
        population = 0L;
        for (PopulationDto p :
                details) {
            population += p.getPopulation();
        }
    }
}
