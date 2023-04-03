package com.citizenv.app.payload;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Time;

public class DeclarationDto {
    private UserDto proceedUser;
    @JsonFormat(pattern="yyyy-MM-dd@HH:mm:ss")
    private Time startTime;
    @JsonFormat(pattern="yyyy-MM-dd@HH:mm:ss")
    private Time endTime;
    private String status;
}
