package com.citizenv.app.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "association_types")
@Data
public class AssociationType {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    private String name;

    @OneToMany(mappedBy = "associationType", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Association> associations;
}
