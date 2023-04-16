package com.citizenv.app.payload;

import com.citizenv.app.entity.AddressType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link AddressType} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressTypeDto implements Serializable {
    private String id;
    private String name;
}