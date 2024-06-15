package com.example.ertebatfardaboarding.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "User")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HIBERNATE_SEQUENCE")
    @SequenceGenerator(name = "HIBERNATE_SEQUENCE", sequenceName = "HIBERNATE_SEQUENCE", allocationSize = 1)
    private Long id;
    @Column
    private String name;
    @Column(unique = true)
    private String email;
    @Column
    private String password;
}
