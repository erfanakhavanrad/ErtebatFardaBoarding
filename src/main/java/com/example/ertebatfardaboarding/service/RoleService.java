package com.example.ertebatfardaboarding.service;

import com.example.ertebatfardaboarding.domain.Role;
import com.example.ertebatfardaboarding.domain.dto.RoleDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

public interface RoleService {
    Page<Role> getRoles(Integer pageNo, Integer perPage) throws Exception;

    Role getRoleById(Long id) throws Exception;

    Role getRoleByName(String name) throws Exception;

    Role createRole(RoleDto roleDto, HttpServletRequest httpServletRequest) throws Exception;

    Role updateRole(RoleDto roleDto, HttpServletRequest httpServletRequest) throws Exception;

    void deleteRole(Long id) throws Exception;
}
