package com.citizenv.app.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "addresses")
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "citizen_id")
    private Citizen citizen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hamlet_id")
    private Hamlet hamlet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_type")
    private AddressType addressType;

    public void setParam (Citizen citizen, Hamlet hamlet, AddressType addressType) {
        this.citizen = citizen;
        this.hamlet = hamlet;
        this.addressType = addressType;
    }
}
