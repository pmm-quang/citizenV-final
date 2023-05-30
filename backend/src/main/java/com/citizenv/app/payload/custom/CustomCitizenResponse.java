package com.citizenv.app.payload.custom;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class CustomCitizenResponse {
    private String nationalId;
    private String name;
    private String dateOfBirth;
    private String sex;

    public CustomCitizenResponse(String nationalId, String name, LocalDate dateOfBirth, String sex) {
        this.nationalId = nationalId;
        this.name = name;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        this.dateOfBirth = dateOfBirth.format(formatter);
        this.sex = sex;
    }
}
