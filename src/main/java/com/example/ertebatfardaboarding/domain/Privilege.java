package com.example.ertebatfardaboarding.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@Table(name = "privilege")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Privilege implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "HIBERNATE_SEQUENCE")
    @SequenceGenerator(name = "HIBERNATE_SEQUENCE", sequenceName = "HIBERNATE_SEQUENCE", allocationSize = 1)
    private Long id;
    @Column
    private String name;
    @Column
    private String domain;
    @Column
    private String access;

    @Override
    public String getAuthority() {
        return domain + "," + access;
    }

    public Privilege(String domain, String access) {
        this.domain = domain;
        this.access = access;
    }

    public Privilege() {
    }
}
