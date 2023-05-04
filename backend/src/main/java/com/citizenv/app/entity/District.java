package com.citizenv.app.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "districts")
@Data
public class District extends AdministrativeDivision {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_code")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Province province;

    @OneToMany(mappedBy = "district", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Ward> wards;
}
