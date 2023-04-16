package com.citizenv.app.payload;

import com.citizenv.app.entity.Address;
import com.citizenv.app.payload.AddressTypeDto;
import com.citizenv.app.payload.CitizenDto;
import com.citizenv.app.payload.HamletDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link Address} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto implements Serializable {
    private CitizenDto citizen;
    private HamletDto hamlet;
    private AddressTypeDto addressType;
}