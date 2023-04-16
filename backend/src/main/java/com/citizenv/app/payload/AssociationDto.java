package com.citizenv.app.payload;

import com.citizenv.app.entity.Association;
import com.citizenv.app.entity.AssociationType;
import com.citizenv.app.payload.CitizenDto;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link Association} entity
 */
@Data
public class AssociationDto implements Serializable {
    private CitizenDto citizen;
    private CitizenDto associatedCitizen;
    private AssociationType associationType;
}