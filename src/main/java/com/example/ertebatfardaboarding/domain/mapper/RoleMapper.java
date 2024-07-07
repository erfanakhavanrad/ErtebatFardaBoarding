package com.example.ertebatfardaboarding.domain.mapper;

import com.example.ertebatfardaboarding.domain.Role;
import com.example.ertebatfardaboarding.domain.dto.RoleDto;
import com.example.ertebatfardaboarding.domain.responseDto.RoleResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleMapper roleMapper = Mappers.getMapper(RoleMapper.class);

    RoleDto roleToRoleDto(Role role);

    RoleResponseDto roleToRoleResponseDto(Role role);

    Role roleDtoToRole(RoleDto roleDto);

    List<RoleResponseDto> roleListToRoleResponseDtoList(List<Role> roleList);

    List<RoleDto> roleListToRoleDtoList(List<Role> roleList);

    void updateRoleFromDto(RoleDto roleDto, @MappingTarget Role role);

}
