package com.example.ertebatfardaboarding.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "Contact")
public class Contact implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HIBERNATE_SEQUENCE")
    @SequenceGenerator(name = "HIBERNATE_SEQUENCE", sequenceName = "HIBERNATE_SEQUENCE", allocationSize = 1)
    private Long id;
    @Column
    private String name;
    @Column
    private String email;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contact", cascade = CascadeType.ALL)
    private List<ContactDetail> contactDetailList;


//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_seq") // AUTO for data insertion in the class projmanagapplication (the commented out part), IDENTITY let hibernate use the database id counter.
//    private long employeeId;                            // The downside of IDENTITY is that if we batch a lot of employees or projects it will be much slower to update them, we use SEQUENCE now that we have schema.sql (spring does batch update)
//
//
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_generator")
//    @SequenceGenerator(name = "employee_generator", sequenceName = "employee_seq", allocationSize = 1)


}
