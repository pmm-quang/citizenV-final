package com.citizenv.app.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "administrative_regions")
@Data
public class AdministrativeRegion {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "administrativeRegion", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Province> provinceList;
}
