package com.example.ertebatfardaboarding.domain.mapper;

import com.example.ertebatfardaboarding.domain.Privilege;
import com.example.ertebatfardaboarding.domain.dto.PrivilegeDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PrivilegeMapper {
    PrivilegeMapper privilegeMapper = Mappers.getMapper(PrivilegeMapper.class);

    PrivilegeDto privilegeToPrivilegeDto(Privilege privilege);

    Privilege privilegeDtoToPrivilege(PrivilegeDto privilegeDto);
}
