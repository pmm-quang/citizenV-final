package com.citizenv.app.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "administrative_divisions")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@ToString
public class AdministrativeDivision {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String code;

    private String name;

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "administrative_unit_id")
    private AdministrativeUnit administrativeUnit;

}
