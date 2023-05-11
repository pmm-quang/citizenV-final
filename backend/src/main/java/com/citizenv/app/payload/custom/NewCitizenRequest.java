package com.citizenv.app.payload.custom;

import com.citizenv.app.payload.AddressDto;
import com.citizenv.app.payload.AssociationDto;
import com.citizenv.app.payload.EthnicityDto;
import com.citizenv.app.payload.ReligionDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class NewCitizenRequest {
    private String id;
    private String name;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private Integer age;
    private String bloodType;
    private String sex;
    private String maritalStatus;
    private Integer ethnicityId;
    private String otherNationality;
    private ReligionDto religion;
    private List<String> addresses;
    private List<AssociationDto> associations;
}
