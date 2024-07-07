package com.example.ertebatfardaboarding.domain.responseDto;

import lombok.Data;

import java.util.List;
@Data
public class ContactResponseDto {
    private Long id;
    private String contactID;
    private String name;
    private String email;
    private List<ContactDetailResponseDto> contactDetailList;
    private Long attachmentId;
}
