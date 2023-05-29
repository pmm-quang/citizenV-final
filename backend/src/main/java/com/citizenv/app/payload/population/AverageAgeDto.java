package com.citizenv.app.payload.population;

import lombok.Data;

import java.text.DecimalFormat;

@Data
public class AverageAgeDto {
    Integer year;
    protected Double averageAge;
    private static DecimalFormat df = new DecimalFormat(".#");

    public AverageAgeDto(double d) {
        averageAge = d;
    }

    public AverageAgeDto(int i, double d) {
        year = i;
        averageAge = d;
    }

    public Double getAverageAge() {
        return Double.valueOf(df.format(averageAge));
    }
}
