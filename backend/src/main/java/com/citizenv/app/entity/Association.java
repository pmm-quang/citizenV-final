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

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "citizen_id")
    private Citizen citizen;

    private String associatedCitizenNationalId;
    private String associatedCitizenName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "association_type")
    private AssociationType associationType;

}
