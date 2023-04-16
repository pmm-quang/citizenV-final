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
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "citizen_id")
    private Citizen citizen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "associated_citizen_id")
    private Citizen associatedCitizen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "association_type")
    private AssociationType associationType;

}
