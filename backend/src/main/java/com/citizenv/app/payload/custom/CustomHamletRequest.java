package com.citizenv.app.payload.custom;

import lombok.Data;

@Data
public class CustomHamletRequest {
    String code;
    String name;
    String wardCode;
    Integer administrativeUnitId;
}
