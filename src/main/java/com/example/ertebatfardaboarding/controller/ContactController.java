package com.example.ertebatfardaboarding.controller;

import com.example.ertebatfardaboarding.domain.ResponseModel;
import com.example.ertebatfardaboarding.domain.dto.ContactDto;
import com.example.ertebatfardaboarding.domain.responseDto.ContactResponseDto;
import com.example.ertebatfardaboarding.exception.ContactException;
import com.example.ertebatfardaboarding.service.ContactService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("contact")
@Slf4j
public class ContactController {

    @Autowired
    ResponseModel responseModel;

    @Autowired
    ContactService contactService;

    @PreAuthorize("hasAuthority('CONTACT,READ')")
    @GetMapping("/getAll")
    public ResponseModel getAll(@RequestParam Integer pageNo, Integer perPage, HttpServletResponse httpServletResponse) throws Exception {
        responseModel.clear();
        log.info("get all contacts");
        Page<ContactResponseDto> contacts = contactService.getContacts(pageNo, perPage);
        responseModel.setContents(contacts.getContent());
        responseModel.setRecordCount((int) contacts.getTotalElements());
        responseModel.setStatus(httpServletResponse.getStatus());
        return responseModel;
    }

    @PreAuthorize("hasAuthority('CONTACT,READ')")
    @GetMapping("/getById")
    public ResponseModel getById(@RequestParam Long id, HttpServletResponse httpServletResponse) throws Exception {
        log.info("get contact by id");
        responseModel.clear();
        responseModel.setContent(contactService.getContactById(id));
        responseModel.setRecordCount(1);
        responseModel.setStatus(httpServletResponse.getStatus());
        return responseModel;
    }

    @PreAuthorize("hasAuthority('CONTACT,READ')")
    @GetMapping("/searchContact")
    public ResponseModel searchContact(@RequestParam Integer pageNo, Integer perPage, @RequestBody ContactDto contactDto, HttpServletResponse httpServletResponse) {
        responseModel.clear();
        Page<ContactResponseDto> contacts = contactService.getContactsBySearch(contactDto, pageNo, perPage);
        responseModel.setContents(contacts.getContent());
        responseModel.setRecordCount((int) contacts.getTotalElements());
        responseModel.setStatus(httpServletResponse.getStatus());
        return responseModel;
    }

    @PreAuthorize("hasAuthority('CONTACT,CREATE')")
    @PostMapping(path = "/save")
    public ResponseModel save(@RequestBody ContactDto contactDto, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        log.info("create contact");
        responseModel.clear();
        responseModel.setContent(contactService.createContact(contactDto, httpServletRequest));
        responseModel.setStatus(httpServletResponse.getStatus());
        return responseModel;
    }

    @PreAuthorize("hasAuthority('CONTACT,CREATE')")
    @PostMapping(path = "/saveWithAttachment")
    public ResponseModel saveWithAttachment(@RequestBody ContactDto contactDto, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        log.info("create contact");
        responseModel.clear();
        responseModel.setContent(contactService.createContactWithAttachment(contactDto, httpServletRequest));
        responseModel.setStatus(httpServletResponse.getStatus());
        return responseModel;
    }

    @PreAuthorize("hasAuthority('CONTACT,UPDATE')")
    @PutMapping("/update")
    public ResponseModel update(@RequestBody ContactDto contactDto, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ContactException {
        log.info("update contact");
        responseModel.clear();
        responseModel.setContent(contactService.updateContactMapStruct(contactDto, httpServletRequest));
        responseModel.setRecordCount(1);
        responseModel.setStatus(httpServletResponse.getStatus());
        return responseModel;
    }

    @PreAuthorize("hasAuthority('CONTACT,DELETE')")
    @DeleteMapping("/delete/{id}")
    public ResponseModel delete(@PathVariable("id") Long id, HttpServletResponse httpServletResponse) {
        log.info("delete contact");
        responseModel.clear();
        contactService.deleteContact(id);
        responseModel.setStatus(httpServletResponse.getStatus());
        return responseModel;
    }
}
