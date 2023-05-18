package com.citizenv.app.payload.population;

import lombok.Data;

@Data
public class PopulationDto {
    private String name;
    private Long population;

    public PopulationDto(String name) {
        this.name = name;
        population = 1L;
    }

    public void increasePopulation(Long count) {
        population += count;
    }
}
