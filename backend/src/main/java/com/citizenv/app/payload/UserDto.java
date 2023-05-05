package com.citizenv.app.payload;
import lombok.Data;

@Data
public class UserDto {
    private String username;
    private Boolean isActive;
    private RoleDto role;
    private AdministrativeDivisionDto administrativeDivision;
}
