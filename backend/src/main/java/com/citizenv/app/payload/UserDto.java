package com.citizenv.app.payload;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String username;
    private String password;
    private Boolean isActive;
    private List<RoleDto> roles;
    private DeclarationDto declaration;
    private AdministrativeDivisionDto division;
}
