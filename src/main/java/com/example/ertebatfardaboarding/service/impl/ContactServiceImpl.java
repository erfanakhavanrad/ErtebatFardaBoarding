package com.example.ertebatfardaboarding.service.impl;

import com.example.ertebatfardaboarding.ErtebatFardaBoardingApplication;
import com.example.ertebatfardaboarding.domain.Contact;
import com.example.ertebatfardaboarding.domain.ResponseModel;
import com.example.ertebatfardaboarding.domain.User;
import com.example.ertebatfardaboarding.domain.dto.ContactDetailDto;
import com.example.ertebatfardaboarding.domain.dto.ContactDto;
import com.example.ertebatfardaboarding.domain.mapper.ContactMapper;
import com.example.ertebatfardaboarding.exception.ContactException;
import com.example.ertebatfardaboarding.exception.UserException;
import com.example.ertebatfardaboarding.repo.ContactRepository;
import com.example.ertebatfardaboarding.repo.UserRepository;
import com.example.ertebatfardaboarding.service.ContactService;
import com.example.ertebatfardaboarding.service.FileStorageService;
import com.example.ertebatfardaboarding.utils.GlobalConstants;
import com.example.ertebatfardaboarding.utils.Utils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    Utils utils;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    ResponseModel responseModel;

    @Resource(name = "faMessageSource")
    private MessageSource faMessageSource;

    @Override
    public ContactDto createContact(ContactDto contactDto, HttpServletRequest httpServletRequest) throws Exception {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUsername);
        for (int i = 0; i < contactDto.getContactDetailList().size(); i++) {
            ContactDetailDto details = contactDto.getContactDetailList().get(i);
            contactDto.getContactDetailList().set(i, details);
        }
        Contact contact = ContactMapper.contactMapper.contactDtoToContact(contactDto);
        contact.setCreatedBy(currentUser);
        Contact savedContact = contactRepository.save(contact);
        ContactDto contactDto1 = ContactMapper.contactMapper.contactToContactDto(savedContact);
        return contactDto1;
    }

    @Override
    @Transactional
    public ContactDto createContactWithAttachment(ContactDto contactDto, HttpServletRequest httpServletRequest) throws Exception {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUsername);

        for (int i = 0; i < contactDto.getContactDetailList().size(); i++) {
            ContactDetailDto details = contactDto.getContactDetailList().get(i);
            if (Objects.equals(details.getNumberName(), "mobile")) {
                if (!isNumberValid(details.getNumber())) {
                    throw new ContactException(faMessageSource.getMessage("PHONE_NOT_VALID", null, Locale.getDefault()));
                }

            }
            contactDto.getContactDetailList().set(i, details);
        }
        Contact contact = ContactMapper.contactMapper.contactDtoToContact(contactDto);
        contact.setCreatedBy(currentUser);
        Contact savedContact = contactRepository.save(contact);
        ContactDto contactDto1 = ContactMapper.contactMapper.contactToContactDto(savedContact);
        return contactDto1;
    }

    private boolean isNumberValid(String stringNumber) {
        String PHONE_NUMBER_PATTERN = "^0\\d{10}$";
        Pattern pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
        if (stringNumber == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(stringNumber);
        return matcher.matches();
    }

    @SneakyThrows
    @Override
    public Contact updateContact(ContactDto contactDto, HttpServletRequest httpServletRequest) throws ContactException {
        Contact oldContact;
        if (!contactRepository.existsById(contactDto.getId())) {
            throw new ContactException(faMessageSource.getMessage("ACCESS_DENIED", null, Locale.ENGLISH));
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

    @SneakyThrows
    @Transactional
    @Override
    public Contact updateContactMapStruct(ContactDto contactDto, HttpServletRequest httpServletRequest) throws ContactException {
        Contact contact = contactRepository.findById(contactDto.getId())
                .orElseThrow(() -> new UserException("User not found"));
        ContactMapper.contactMapper.updateContactDetailFromDto(contactDto, contact);
        return contactRepository.save(contact);
    }


    @Override
    public Page<Contact> getContacts(Integer pageNo, Integer perPage) throws Exception {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUsername);
        Page<Contact> all;
        if (currentUser.getRoles().stream().anyMatch(role -> role.getName().equals(GlobalConstants.ADMIN_NAME))) {
            all = contactRepository.findAll(ErtebatFardaBoardingApplication.createPagination(pageNo, perPage));
        } else {
            all = contactRepository.findAllByCreatedBy(currentUser, ErtebatFardaBoardingApplication.createPagination(pageNo, perPage));
        }
        return all;
    }

    @Override
    public List<Contact> getContactsBySearch(ContactDto contactDto) {
        Specification<Contact> specification = Specification.where(null);

        if (contactDto.getName() != null) {
            specification = hasName(contactDto.getName());
        }

        if (contactDto.getEmail() != null) {
            specification = hasEmail(contactDto.getEmail());
        }
        return contactRepository.findAll(specification);
    }

    @Override
    public Contact getContactById(Long id) throws Exception {
        return contactRepository.findById(id).orElseThrow(() -> new Exception(faMessageSource.getMessage("NOT_FOUND", null, Locale.ENGLISH)));
    }

    @Override
    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }

    private static Specification<Contact> hasName(String name) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                "%" + name.toLowerCase() + "%"));
    }

    private static Specification<Contact> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("email")),
                "%" + email.toLowerCase() + "%");
    }

}
