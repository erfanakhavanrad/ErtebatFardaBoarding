package com.example.ertebatfardaboarding.domain.mapper;

import com.example.ertebatfardaboarding.domain.Privilege;
import com.example.ertebatfardaboarding.domain.dto.PrivilegeDto;
import com.example.ertebatfardaboarding.domain.responseDto.PrivilegeResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PrivilegeMapper {
    PrivilegeMapper privilegeMapper = Mappers.getMapper(PrivilegeMapper.class);

    PrivilegeDto privilegeToPrivilegeDto(Privilege privilege);

    PrivilegeResponseDto privilegeToPrivilegeResponseDto(Privilege privilege);

    Privilege privilegeDtoToPrivilege(PrivilegeDto privilegeDto);

    void updatePrivilegeFromDto(PrivilegeDto privilegeDto, @MappingTarget Privilege privilege);
}
