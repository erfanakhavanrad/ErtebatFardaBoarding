package com.example.ertebatfardaboarding.domain.mapper;

import com.example.ertebatfardaboarding.domain.Contact;
import com.example.ertebatfardaboarding.domain.ContactDetail;
import com.example.ertebatfardaboarding.domain.dto.ContactDto;
import com.example.ertebatfardaboarding.domain.responseDto.ContactResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContactMapper {
    ContactMapper contactMapper = Mappers.getMapper(ContactMapper.class);

    //    @Mapping(source = "id", target = "id")
    ContactDto contactToContactDto(Contact contact);

    Contact contactDtoToContact(ContactDto contactDto);
    ContactResponseDto contactDtoToContactResponseDto(ContactDto contactDto);
    ContactResponseDto contactToContactResponseDto(Contact contactDto);

    void updateContactDetailFromDto(ContactDto contactDto, @MappingTarget Contact contact);

    List<ContactResponseDto> contactListToContactResponseDtoList(List<Contact> contactList);

}
