package com.citizenv.app.payload.custom;

import lombok.Data;

@Data
public class CustomAddress {
//    private Integer id;
    private Integer addressType;
    private String hamletCode;
    private String provinceCode;
}
