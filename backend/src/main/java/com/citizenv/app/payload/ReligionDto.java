package com.citizenv.app.payload;

import com.citizenv.app.entity.Religion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link Religion} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReligionDto implements Serializable {
    private String id;
    private String name;
}