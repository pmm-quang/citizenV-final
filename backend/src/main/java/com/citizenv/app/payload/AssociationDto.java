package com.citizenv.app.payload;

import com.citizenv.app.entity.Association;
import com.citizenv.app.entity.AssociationType;
import com.citizenv.app.payload.CitizenDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link Association} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssociationDto implements Serializable {
//    private CitizenDto citizen;
    private Integer id;
    private String associatedCitizenId;
    private String associatedCitizenName;
    private AssociationTypeDto associationType;
}