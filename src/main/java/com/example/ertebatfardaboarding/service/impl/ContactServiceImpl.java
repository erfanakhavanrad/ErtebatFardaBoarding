package com.example.ertebatfardaboarding.service.impl;

import com.example.ertebatfardaboarding.ErtebatFardaBoardingApplication;
import com.example.ertebatfardaboarding.domain.Contact;
import com.example.ertebatfardaboarding.domain.ResponseModel;
import com.example.ertebatfardaboarding.domain.dto.ContactDetailDto;
import com.example.ertebatfardaboarding.domain.dto.ContactDto;
import com.example.ertebatfardaboarding.domain.mapper.ContactMapper;
import com.example.ertebatfardaboarding.exception.ContactException;
import com.example.ertebatfardaboarding.repo.ContactRepository;
import com.example.ertebatfardaboarding.service.ContactService;
import com.example.ertebatfardaboarding.service.FileStorageService;
import com.example.ertebatfardaboarding.utils.Utils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    Utils utils;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    ResponseModel responseModel;

    @Resource(name = "faMessageSource")
    private MessageSource faMessageSource;
    private Integer pageNo;
    private Integer perPage;

    @Override
    public ContactDto createContact(ContactDto contactDto, HttpServletRequest httpServletRequest) throws Exception {
        for (int i = 0; i < contactDto.getContactDetailList().size(); i++) {
            ContactDetailDto details = contactDto.getContactDetailList().get(i);
            contactDto.getContactDetailList().set(i, details);
        }
        Contact contact = ContactMapper.contactMapper.contactDtoToContact(contactDto);
        Contact savedContact = contactRepository.save(contact);
        ContactDto contactDto1 = ContactMapper.contactMapper.contactToContactDto(savedContact);
        return contactDto1;
    }

    @Override
    @Transactional
    public ContactDto createContactWithAttachment(ContactDto contactDto, HttpServletRequest httpServletRequest) throws Exception {

        for (int i = 0; i < contactDto.getContactDetailList().size(); i++) {
            ContactDetailDto details = contactDto.getContactDetailList().get(i);
            contactDto.getContactDetailList().set(i, details);
        }
        Contact contact = ContactMapper.contactMapper.contactDtoToContact(contactDto);
        Contact savedContact = contactRepository.save(contact);
        ContactDto contactDto1 = ContactMapper.contactMapper.contactToContactDto(savedContact);
        return contactDto1;

//        for (int i = 0; i < contactDto.getContactDetailList().size(); i++) {
//            ContactDetailDto details = contactDto.getContactDetailList().get(i);
//            contactDto.getContactDetailList().set(i, details);
//        }
//        Contact contact = ContactMapper.contactMapper.contactDtoToContact(contactDto);
//        Contact savedContact = contactRepository.save(contact);
//        ContactDto contactDto1 = ContactMapper.contactMapper.contactToContactDto(savedContact);
//        Attachment attachment = AttachmentMapper.attachmentMapper.attachmentDtoToAttachment(attachmentDto);
//        contactDto1.setAttachment(attachment);
//        return contactDto1;
    }

    @SneakyThrows
    @Override
    public Contact updateContact(ContactDto contactDto, HttpServletRequest httpServletRequest) throws ContactException {
//        Contact oldContact = getContactById(contactDto.getId());
        Contact oldContact;
        if (!contactRepository.existsById(contactDto.getId())) {
            throw new ContactException("Contact not found here.");
        }
        oldContact = getContactById(contactDto.getId());
        Contact newContact = ContactMapper.contactMapper.contactDtoToContact(contactDto);
        responseModel.clear();
        Contact updated = (Contact) responseModel.merge(oldContact, newContact);
        if (newContact.getContactDetailList() != null && !newContact.getContactDetailList().isEmpty()) {
            updated.setContactDetailList(newContact.getContactDetailList());
        }
        return contactRepository.save(updated);
    }

//    @Override
//    public Contact updateContact(ContactDto contactDto, HttpServletRequest httpServletRequest) throws Exception {
//        Contact oldContact = getContactById(contactDto.getId());
//        Contact newContact = ContactMapper.contactMapper.contactDtoToContact(contactDto);
//        responseModel.clear();
//        Contact updated = (Contact) responseModel.merge(oldContact, newContact);
//        if (newContact.getContactDetailList() != null && !newContact.getContactDetailList().isEmpty()) {
//            updated.setContactDetailList(newContact.getContactDetailList());
//        }
//        return contactRepository.save(updated);
//    }

    @Override
    public Page<Contact> getContacts(Integer pageNo, Integer perPage) throws Exception {
        this.pageNo = pageNo;
        this.perPage = perPage;
        return contactRepository.findAll(ErtebatFardaBoardingApplication.createPagination(pageNo, perPage));
    }

    @Override
    public Contact getContactById(Long id) throws Exception {
        return contactRepository.findById(id).orElseThrow(() -> new Exception(faMessageSource.getMessage("NOT_FOUND", null, Locale.ENGLISH)));
    }

    @Override
    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }


}
