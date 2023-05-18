package com.citizenv.app.entity;

import com.citizenv.app.component.Utils;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nationalId;

    @Column(nullable = false)
    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    /*Tuổi của công dân, tự động tính khi Getter của Citizen được gọi*/
    @Transient
    private Integer age;
    public Integer getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    private String bloodType;

    private String sex;

    private String maritalStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ethnicity_id", nullable = false)
    private Ethnicity ethnicity;

    private String otherNationality;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "religion_id")
    private Religion religion;

    @OneToMany(mappedBy = "citizen", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Address> addresses;

    @OneToMany(mappedBy = "citizen", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Association> associations;

    public Religion getReligion() {
        return religion == null ? new Religion(0, Utils.RELIGION_NONE) : religion;
    }

    public String getOtherNationality() {
        return otherNationality == null ? "" : otherNationality;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
//        Citizen citizen = (Citizen) o;
//        return id != null && Objects.equals(id, citizen.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return getClass().hashCode();
//    }
}
