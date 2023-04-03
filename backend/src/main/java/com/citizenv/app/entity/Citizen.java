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

    private String ethnic;

    private String otherNationality;

    private String religion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "father_id")
    private Citizen father;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mother_id")
    private Citizen mother;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marriage_partner_id")
    private Citizen marriagePartner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "legal_representative_id")
    private Citizen legalRepresentative;

    @OneToMany(mappedBy = "father", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Citizen> fatherOf;

    @OneToMany(mappedBy = "mother", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Citizen> motherOf;

    @OneToMany(mappedBy = "marriagePartner", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Citizen> marriagePartnerOf;

    @OneToMany(mappedBy = "legalRepresentative", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Citizen> legalRepresentativeOf;

    @OneToMany(mappedBy = "citizen", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Address> addresses;

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
