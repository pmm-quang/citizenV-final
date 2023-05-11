package com.citizenv.app.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "declarations")
@Data
public class Declaration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "grant_code")
//    private User grantUser;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "proceed_code")
//    private User proceedUser;

    @Column(name = "start_time", columnDefinition = "DATETIME")
    private LocalTime startTime;

    @Column(name = "end_time", columnDefinition = "DATETIME")
    private LocalTime endTime;

    private String status;
}
