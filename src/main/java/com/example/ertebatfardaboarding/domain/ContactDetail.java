package com.example.ertebatfardaboarding.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ContactDetail")
public class ContactDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HIBERNATE_SEQUENCE")
    @SequenceGenerator(name = "HIBERNATE_SEQUENCE", sequenceName = "HIBERNATE_SEQUENCE", allocationSize = 1)
    private Long id;
    @Column
    private String numberName;
    @Column
    private String number;
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "contact_id")
//    private Contact contact;
}
