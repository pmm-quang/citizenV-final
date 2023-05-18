package com.citizenv.app.payload.population;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AgeGroupDto {
    private Integer year;
    private List<PopulationDto> ageGroupPopulation;

    public AgeGroupDto(Integer year) {
        this.year = year;
        ageGroupPopulation = new ArrayList<>();
    }

    public AgeGroupDto(Integer year, List<PopulationDto> populationDtoList) {
        this.year = year;
        ageGroupPopulation = populationDtoList;
    }
}
