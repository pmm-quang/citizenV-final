package com.citizenv.app.payload.population;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class DivisionPopulationByCitizenPropertyDto extends DivisionGeneralPopulationDto {
    private List<Map<String, Object>> details;

    public DivisionPopulationByCitizenPropertyDto(List<Map<String, Object>> populationDtos) {
        details = populationDtos;
    }

    public DivisionPopulationByCitizenPropertyDto(String c, String n, List<Map<String, Object>> populationDtos) {
        super(c, n);
        details = populationDtos;
    }

    public DivisionPopulationByCitizenPropertyDto(String c, String n, Long l, List<Map<String, Object>> populationDtos) {
        super(c, n, l);
        details = populationDtos;
    }

    public void recalculatePopulation() {
        /*population = 0L;
        for (Map<String, Object> p :
                details) {
            BigInteger currentPopulation = (BigInteger) p[p.length - 1];
            population += currentPopulation.longValue() ;
        }
        System.out.println(population);*/
    }
}
