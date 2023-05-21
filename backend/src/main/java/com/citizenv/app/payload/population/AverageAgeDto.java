package com.citizenv.app.payload.population;

import lombok.Data;

import java.text.DecimalFormat;

@Data
public class AverageAgeDto {
    private String code;
    private String name;
    private Double averageAge;
    private static DecimalFormat df = new DecimalFormat(".#");

    public AverageAgeDto(String s, String s1, double d) {
        code = s;
        name = s1;
        averageAge = d;
    }

    public Double getAverageAge() {
        return Double.valueOf(df.format(averageAge));
    }
}
