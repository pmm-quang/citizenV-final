package com.citizenv.app.payload.excel;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ExcelCitizen {
    private String nationalId;
    private String name;
    private LocalDate dateOfBirth;
    private String sex;
    private String bloodType;
    private String maritalStatus;
    private String ethnicity;
    private String religion;
    private String otherNationality;
    private String educationalLevel;
    private String job;
    private List<String> addresses;
    private List<String> associations;

    private String listAddress() {
        String s = "";
        for (String a: addresses) {
            s += "[" + a +"],";
        }
        return "{" + s + "}";
    }
    private String listAssociation() {
        String s = "";
        for (String a: associations) {
            s += "[" + a.toString() +"],";
        }
        return "{" + s + "}";
    }


    @Override
    public String toString() {
        return nationalId+ ", " +name+", "+dateOfBirth+", "+sex+", "+bloodType+", "+maritalStatus+
                ", "+ethnicity+", "+religion+", "+otherNationality+", "+educationalLevel+", "+job
                + ", " +listAddress()+", " + listAssociation();
    }
}
