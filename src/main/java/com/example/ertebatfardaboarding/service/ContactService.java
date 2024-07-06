package com.example.ertebatfardaboarding.service;

import com.example.ertebatfardaboarding.domain.dto.ContactDto;
import com.example.ertebatfardaboarding.domain.responseDto.ContactResponseDto;
import com.example.ertebatfardaboarding.exception.ContactException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ContactService {
    ContactResponseDto createContact(ContactDto contactDto, HttpServletRequest httpServletRequest) throws Exception;

    ContactResponseDto updateContactMapStruct(ContactDto contactDto, HttpServletRequest httpServletRequest) throws ContactException;

    Page<ContactResponseDto> getContacts(Integer pageNo, Integer perPage) throws Exception;

    ContactResponseDto getContactById(Long id) throws Exception;

    List<ContactResponseDto> getContactsBySearch(ContactDto contactDto);

    void deleteContact(Long id);

    ContactResponseDto createContactWithAttachment(ContactDto contactDto, HttpServletRequest httpServletRequest) throws Exception;
}

