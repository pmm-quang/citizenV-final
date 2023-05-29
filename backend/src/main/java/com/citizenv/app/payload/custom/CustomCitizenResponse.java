package com.citizenv.app.payload.custom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomCitizenResponse {
    private String nationalId;
    private String name;
    private String sex;

    public CustomCitizenResponse(String nationalId, String name, String sex) {
        this.nationalId = nationalId;
        this.name = name;
        this.sex = sex;
    }
}
