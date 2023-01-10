package com.example.telegrambot.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity

public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String area;

    private String speciality;

    private String hospitalName;

    private String status;

    @Column(unique = true)
    private String phone;

 /*   @ManyToOne(optional = false)
    private Worker worker;
*/

}
