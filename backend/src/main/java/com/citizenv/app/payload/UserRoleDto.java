package com.citizenv.app.payload;

import com.citizenv.app.entity.Role;
import com.citizenv.app.entity.User;
import lombok.Data;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

@Data
public class UserRoleDto {
    private Long id;
    private RoleDto role;
    private Timestamp grantTime;
    private Timestamp revokeTime;
}
