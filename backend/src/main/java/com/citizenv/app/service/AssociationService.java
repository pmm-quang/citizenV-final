package com.citizenv.app.service;

import com.citizenv.app.payload.AssociationDto;
import com.citizenv.app.payload.AssociationTypeDto;

import java.util.List;

public interface AssociationService {
    List<AssociationDto> getAllAssociation();
    List<AssociationTypeDto> getAllAssociationType();
}
