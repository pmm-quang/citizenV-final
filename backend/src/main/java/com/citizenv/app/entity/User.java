package com.citizenv.app.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotNull(message = "Username cannot be null")
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @Column(nullable = false)
    private String password;

    private Boolean isActive;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "division_id")
    private AdministrativeDivision division;

    @ManyToMany(fetch =  FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id")
            , inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Declaration declaration;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

}
