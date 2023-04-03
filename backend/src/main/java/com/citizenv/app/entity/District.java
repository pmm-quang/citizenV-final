package com.citizenv.app.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "districts")
@Data
public class District {
    @Id
    @Column(name = "code", nullable = false)
    private String code;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_code")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Province province;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administrative_unit_id")
    private AdministrativeUnit administrativeUnit;

    @OneToMany(mappedBy = "district", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Address> addresses;

    @OneToMany(mappedBy = "district", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<User> users;
}
