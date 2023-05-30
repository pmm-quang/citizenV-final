package com.citizenv.app.payload.custom;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CustomCitizenResponse {
    private String nationalId;
    private String name;
    private LocalDate dateOfBirth;
    private String sex;

    public CustomCitizenResponse(String nationalId, String name, LocalDate dateOfBirth, String sex) {
        this.nationalId = nationalId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
    }
}
