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
public class Province extends AdministrativeDivision {
    @ManyToOne()
    @JoinColumn(name = "administrative_region_id")
    private AdministrativeRegion administrativeRegion;

    @OneToMany(mappedBy = "province", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<District> districts;

    private String administrativeCode;
}
