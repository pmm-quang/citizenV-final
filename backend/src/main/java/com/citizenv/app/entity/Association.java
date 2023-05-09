package com.citizenv.app.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "associations")
@Data
public class Association {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "citizen_id")
    private Citizen citizen;

    private String associatedCitizenName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "association_type")
    private AssociationType associationType;

}
