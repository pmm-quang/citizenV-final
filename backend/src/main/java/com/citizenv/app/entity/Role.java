package com.citizenv.app.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@ToString
public class Role {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<User> users;
}
