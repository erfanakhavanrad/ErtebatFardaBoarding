package com.example.ertebatfardaboarding.service;

import com.example.ertebatfardaboarding.domain.Contact;
import com.example.ertebatfardaboarding.domain.User;
import com.example.ertebatfardaboarding.domain.dto.ContactDto;
import com.example.ertebatfardaboarding.domain.dto.UserDto;
import com.example.ertebatfardaboarding.exception.ContactException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ContactService {
    ContactDto createContact(ContactDto contactDto, HttpServletRequest httpServletRequest) throws Exception;

    Contact updateContact(ContactDto contactDto, HttpServletRequest httpServletRequest) throws ContactException;

    Contact updateContactMapStruct(ContactDto contactDto, HttpServletRequest httpServletRequest) throws ContactException;

    Page<Contact> getContacts(Integer pageNo, Integer perPage) throws Exception;

    Contact getContactById(Long id) throws Exception;

    List<Contact> getContactsBySearch(ContactDto contactDto);

    void deleteContact(Long id);

    ContactDto createContactWithAttachment(ContactDto contactDto, HttpServletRequest httpServletRequest) throws Exception;
}

