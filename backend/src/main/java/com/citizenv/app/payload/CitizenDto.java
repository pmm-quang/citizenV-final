package com.citizenv.app.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CitizenDto {
    private String id;
    private String name;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String sex;
    private String maritalStatus;
    private String ethnic;
    private String otherNationality;
    private String religion;
}
