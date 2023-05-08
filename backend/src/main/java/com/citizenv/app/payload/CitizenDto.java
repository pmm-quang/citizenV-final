package com.citizenv.app.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CitizenDto {
    private String id;
    private String name;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private Integer age;
    private String bloodType;
    private String sex;
    private String maritalStatus;
    private EthnicityDto ethnicity;
    private String otherNationality;
    private ReligionDto religion;
    private List<AddressDto> addresses;
    private List<AssociationDto> associations;
}
