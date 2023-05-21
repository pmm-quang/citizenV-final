package com.citizenv.app.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "wards")
//@PrimaryKeyJoinColumn(name = "id")
@Data
public class Ward extends AdministrativeDivision {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    private District district;

    @OneToMany(mappedBy = "ward", fetch = FetchType.LAZY)
    private List<Hamlet> hamlets;
}
