package com.citizenv.app.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "address_types")
@Data
public class AddressType {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    private String name;

    @OneToMany(mappedBy = "addressType", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Address> addresses;
}
