package com.example.ertebatfardaboarding.domain.mapper;

import com.example.ertebatfardaboarding.domain.Contact;
import com.example.ertebatfardaboarding.domain.dto.ContactDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ContactMapper {
    ContactMapper contactMapper = Mappers.getMapper(ContactMapper.class);

//    @Mapping(source = "id", target = "id")
    ContactDto contactToContactDto(Contact contact);

    Contact contactDtoToContact(ContactDto contactDto);
}
