package com.example.ertebatfardaboarding.service;

import com.example.ertebatfardaboarding.domain.dto.RoleDto;
import com.example.ertebatfardaboarding.domain.responseDto.RoleResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoleService {
    Page<RoleResponseDto> getRoles(Integer pageNo, Integer perPage) throws Exception;

    RoleResponseDto getRoleById(Long id) throws Exception;

    List<RoleResponseDto> getRolesBySearch(RoleDto roleDto);

    RoleResponseDto getRoleByName(String name) throws Exception;

    RoleResponseDto createRole(String roleName, Long[] ids, HttpServletRequest httpServletRequest) throws Exception;

    RoleResponseDto updateRole(RoleDto roleDto, HttpServletRequest httpServletRequest) throws Exception;

    void deleteRole(Long id) throws Exception;
}
