package com.example.ertebatfardaboarding.utils;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Attachment")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HIBERNATE_SEQUENCE")
    @SequenceGenerator(name = "HIBERNATE_SEQUENCE", sequenceName = "HIBERNATE_SEQUENCE", allocationSize = 1)
    private Long id;
    @Column
    private String fileName;
    @Column
    private String fileType;
    @Column
    private String accessUrl;
    @Column
    private String username;
    @Column
    private String token;

    public Attachment(String fileName, String fileType, String accessUrl) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.accessUrl = accessUrl;
    }

    public Attachment() {
    }
}
