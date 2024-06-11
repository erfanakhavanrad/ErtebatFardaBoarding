package com.example.ertebatfardaboarding.service.impl;

import com.example.ertebatfardaboarding.ErtebatFardaBoardingApplication;
import com.example.ertebatfardaboarding.domain.Contact;
import com.example.ertebatfardaboarding.domain.dto.ContactDetailDto;
import com.example.ertebatfardaboarding.domain.dto.ContactDto;
import com.example.ertebatfardaboarding.domain.mapper.ContactMapper;
import com.example.ertebatfardaboarding.repo.ContactRepository;
import com.example.ertebatfardaboarding.service.ContactService;
import com.example.ertebatfardaboarding.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    Utils utils;
    @Autowired
    ContactRepository contactRepository;

    @Override
    public ContactDto createContact(ContactDto contactDto, HttpServletRequest httpServletRequest) throws Exception {

        for (int i = 0; i < contactDto.getContactDetailList().size(); i++) {
            ContactDetailDto details = contactDto.getContactDetailList().get(i);
//            details.setID(utils.generateAddressId(5));
            contactDto.getContactDetailList().set(i, details);
        }

        Contact contact = ContactMapper.contactMapper.contactDtoToContact(contactDto);

//        String contactId = utils.generateContactId(5);
//        contact.setContactID(contactId);

        Contact savedContact = contactRepository.save(contact);
        ContactDto contactDto1 = ContactMapper.contactMapper.contactToContactDto(savedContact);
        return contactDto1;
    }

    @Override
    public Page<Contact> getContacts(Integer pageNo, Integer perPage) throws Exception {
        return contactRepository.findAll(ErtebatFardaBoardingApplication.createPagination(pageNo,perPage));
    }

}
