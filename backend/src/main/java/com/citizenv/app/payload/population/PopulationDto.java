package com.citizenv.app.payload.population;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PopulationDto {
    protected String name;
    protected Long population;

    public PopulationDto(String name) {
        this.name = name;
        population = 0L;
    }

    public PopulationDto(String name, long initPopulation) {
        this.name = name;
        population = initPopulation;
    }

    public void increasePopulation(Long count) {
        population += count;
    }
}
