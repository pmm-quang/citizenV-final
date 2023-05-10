package com.citizenv.app.entity.custom;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Population {
    private String name;
    private Long population;

    public void increasePopulation(Long amount) {
        population += amount;
    }
}
