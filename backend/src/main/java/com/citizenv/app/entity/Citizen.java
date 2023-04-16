package com.citizenv.app.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "citizens")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Citizen {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private String sex;

    private String maritalStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ethnicity_id")
    private Ethnicity ethnicity;

    private String otherNationality;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "religion_id")
    private Religion religion;

    @OneToMany(mappedBy = "citizen", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Address> addresses;

    @OneToMany(mappedBy = "citizen", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Association> associations;

    @OneToMany(mappedBy = "associatedCitizen", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Association> associationOf;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Citizen citizen = (Citizen) o;
        return id != null && Objects.equals(id, citizen.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
