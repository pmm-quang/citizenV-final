package com.citizenv.app.payload;

import com.citizenv.app.entity.Role;
import com.citizenv.app.entity.User;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

public class UserRoleDto {
    private Long id;
    private RoleDto role;
    private Timestamp grantTime;
    private Timestamp revokeTime;
}
