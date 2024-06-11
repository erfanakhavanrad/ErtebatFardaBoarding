package com.example.ertebatfardaboarding.service;

import com.example.ertebatfardaboarding.domain.Contact;
import com.example.ertebatfardaboarding.domain.dto.ContactDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

public interface ContactService {
    ContactDto createContact(ContactDto contactDto, HttpServletRequest httpServletRequest) throws Exception;

    Page<Contact> getContacts(Integer pageNo, Integer perPage) throws Exception;
}
