package com.citizenv.app.payload.population;

public class AverageAgeByYearDto extends AverageAgeDto {

    Integer year;
    public AverageAgeByYearDto(String s, String s1, double d) {
        super(s, s1, d);
    }

    public AverageAgeByYearDto(String s, String s1, int year, double d) {
        super(s, s1, d);
        this.year = year;
    }
}
