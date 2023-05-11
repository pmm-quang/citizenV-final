package com.citizenv.app.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    private String password;

    private Boolean isActive;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<UserRole> userRoles;

    @OneToOne
    @JoinColumn(name = "division_code")
    private AdministrativeDivision division;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "role_id")
//    private Role role;

//    @OneToMany(mappedBy = "grantUser", fetch = FetchType.LAZY)
//    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
//    private List<Declaration> grantedDeclarations;
//
//    @OneToMany(mappedBy = "proceedUser", fetch = FetchType.LAZY)
//    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
//    private List<Declaration> proceedDeclarations;
}
