package com.citizenv.app.payload;

import com.citizenv.app.entity.Ethnicity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link Ethnicity} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EthnicityDto implements Serializable {
    private Integer id;
    private String name;
}