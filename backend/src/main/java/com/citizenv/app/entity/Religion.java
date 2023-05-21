package com.citizenv.app.entity;

import com.citizenv.app.component.Utils;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "religions")
@Getter
@Setter
@RequiredArgsConstructor
public class Religion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "religion", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Citizen> citizens;

    public Religion(int i, String religion) {
        id = i;
        name = religion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Religion religion = (Religion) o;
        return getId() != null && Objects.equals(getId(), religion.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
