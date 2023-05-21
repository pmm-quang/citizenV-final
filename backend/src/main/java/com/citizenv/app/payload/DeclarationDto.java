package com.citizenv.app.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class DeclarationDto {
//    private UserDto user;
    @JsonFormat(pattern="yyyy-MM-dd@HH:mm:ss")
    private Timestamp startTime;
    @JsonFormat(pattern="yyyy-MM-dd@HH:mm:ss")
    private Timestamp endTime;
    private String status;
}
