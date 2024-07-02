package com.example.ertebatfardaboarding.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User createdBy;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ContactDetail> contactDetailList;
    @Column
    private Long attachmentId;
}
