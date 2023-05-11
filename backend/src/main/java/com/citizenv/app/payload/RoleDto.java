package com.citizenv.app.payload;

import com.citizenv.app.entity.Role;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link Role} entity
 */
@Data
public class RoleDto {
    private final Long id;
    private final String name;
    private final String description;
}