package com.citizenv.app.payload;

import com.citizenv.app.entity.Role;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the {@link Role} entity
 */
@Data
public class RoleDto {
    private Long id;
    private String role;
    private String description;
    private List<PermissionDto> permissions;
}