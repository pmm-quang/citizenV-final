package com.citizenv.app.payload;

import com.citizenv.app.entity.AssociationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link AssociationType} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssociationTypeDto implements Serializable {
    private String id;
    private String name;
}