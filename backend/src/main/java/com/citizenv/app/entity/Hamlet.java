package com.citizenv.app.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "hamlets")
//@PrimaryKeyJoinColumn(name = "id")
@Data
public class Hamlet extends AdministrativeDivision {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Ward ward;

    @OneToMany(mappedBy = "hamlet", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Address> addresses;
}
