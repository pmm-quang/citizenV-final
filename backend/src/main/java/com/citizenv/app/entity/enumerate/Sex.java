package com.citizenv.app.entity.enumerate;

public enum Sex {
    MALE("Nam"),
    FEMALE("Nữ"),
    OTHER("Khác");

    private final String value;
    Sex(String s) {
        this.value = s;
    }

    public String getValue() {
        return value;
    }
}