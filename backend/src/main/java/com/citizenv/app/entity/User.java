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
    @Id
    @Column(name = "username", nullable = false)
    private String username;

    private String password;

    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_code")
    private Province province;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_code")
    private District district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_code")
    private Ward ward;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hamlet_code")
    private Hamlet hamlet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "grantUser", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Declaration> grantedDeclarations;

    @OneToMany(mappedBy = "proceedUser", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Declaration> proceedDeclarations;
}
