package com.citizenv.app.entity;

import com.citizenv.app.entity.enumerate.BloodType;
import com.citizenv.app.entity.enumerate.MaritalStatus;
import com.citizenv.app.entity.enumerate.Sex;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_type", columnDefinition = "ENUM('A', 'B', 'O', 'AB')", nullable = false)
    private BloodType bloodType;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex", columnDefinition = "ENUM('Nam', 'Nữ', 'Khác')", nullable = false)
    private Sex sex;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status", columnDefinition = "ENUM('Chưa kết hôn', 'Đã kết hôn', 'Ly hôn')", nullable = false)
    private MaritalStatus maritalStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ethnicity_id", nullable = false)
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
