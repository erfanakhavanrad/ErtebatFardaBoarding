package com.example.ertebatfardaboarding.domain.mapper;

import com.example.ertebatfardaboarding.domain.Role;
import com.example.ertebatfardaboarding.domain.dto.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleMapper {
    RoleMapper roleMapper = Mappers.getMapper(RoleMapper.class);

    RoleDto roleToRoleDto(Role role);

    Role roleDtoToRole(RoleDto roleDto);

}
