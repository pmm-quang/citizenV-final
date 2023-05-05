package com.citizenv.app.payload;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Time;
import java.time.LocalTime;

public class DeclarationDto {
    private UserDto proceedUser;
    @JsonFormat(pattern="yyyy-MM-dd@HH:mm:ss")
    private LocalTime startTime;
    @JsonFormat(pattern="yyyy-MM-dd@HH:mm:ss")
    private LocalTime endTime;
    private String status;
}
