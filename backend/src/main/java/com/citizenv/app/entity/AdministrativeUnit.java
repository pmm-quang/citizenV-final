package com.citizenv.app.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "administrative_units")
@Data
public class AdministrativeUnit {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    private String fullName;
    private String fullNameEn;
    private String shortName;
    private String shortNameEn;
    private String codeName;
    private String codeNameEn;

    @OneToMany(mappedBy = "administrativeUnit", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Province> provinceList;
}
