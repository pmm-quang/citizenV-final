package com.citizenv.app.payload;

import com.citizenv.app.entity.enumerate.BloodType;
import com.citizenv.app.entity.enumerate.MaritalStatus;
import com.citizenv.app.entity.enumerate.Sex;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CitizenDto {
    private String id;
    private String name;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private BloodType bloodType;
    private Sex sex;
    private MaritalStatus maritalStatus;
    private EthnicityDto ethnicity;
    private String otherNationality;
    private ReligionDto religion;
}
