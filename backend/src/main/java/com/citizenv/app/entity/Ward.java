package com.citizenv.app.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "wards")
@Data
public class Ward {
    @Id
    @Column(name = "code", nullable = false)
    private String code;

    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_code")
    private District district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administrative_unit_id")
    private AdministrativeUnit administrativeUnit;

    @OneToMany(mappedBy = "ward", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Address> addresses;

    @OneToMany(mappedBy = "ward", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<User> users;
}
