package com.citizenv.app.entity.enumerate;

public enum MaritalStatus {
    SINGLE("Chưa kết hôn"),
    MARRIED("Đã kết hôn"),
    DIVORCED("Ly hôn");

    private final String value;
    MaritalStatus(String s) {
        this.value = s;
    }

    public String getValue() {
        return value;
    }
}