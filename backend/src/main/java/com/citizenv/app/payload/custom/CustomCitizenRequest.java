package com.citizenv.app.payload.custom;

import com.citizenv.app.payload.AssociationDto;
import com.citizenv.app.payload.ReligionDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CustomCitizenRequest {
    private String nationalId;
    private String name;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String bloodType;
    private String sex;
    private String maritalStatus;
    private Integer ethnicityId;
    private String otherNationality;
    private Integer religionId;
    private String job;
    private String educationalLevel;
    private List<CustomAddress> addresses;
    private List<AssociationDto> associations;
}
