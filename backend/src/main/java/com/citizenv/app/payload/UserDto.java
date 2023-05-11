package com.citizenv.app.payload;
import com.citizenv.app.entity.AdministrativeDivision;
import com.citizenv.app.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private Boolean isActive;
    private List<UserRoleDto> userRoles;
    private AdministrativeDivisionDto division;
}
