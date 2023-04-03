package com.citizenv.app.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "provinces")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Province {
    @Id
    @Column(name = "code", nullable = false)
    private String code;

    private String name;

    @ManyToOne()
    @JoinColumn(name = "administrative_unit_id")
    private AdministrativeUnit administrativeUnit;

    @ManyToOne()
    @JoinColumn(name = "administrative_region_id")
    private AdministrativeRegion administrativeRegion;

    @OneToMany(mappedBy = "province", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<District> districts;

    @OneToMany(mappedBy = "province", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Address> addresses;

    @OneToMany(mappedBy = "province", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<User> users;

    public void setAdministrativeUnit(AdministrativeUnit administrativeUnit) {
        this.administrativeUnit = administrativeUnit;
    }
}
