package com.example.ertebatfardaboarding.domain.dto;

import lombok.Data;

import java.util.List;
@Data
public class ContactDto {
    private Long id;
    private String contactID;
    private String name;
    private String email;
    private List<ContactDetailDto> contactDetailList;
}
