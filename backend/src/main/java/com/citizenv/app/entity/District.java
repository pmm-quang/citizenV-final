package com.citizenv.app.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "districts")
//@PrimaryKeyJoinColumn(name = "id")
@Data
public class District extends AdministrativeDivision {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Province province;

    @OneToMany(mappedBy = "district", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Ward> wards;
}
