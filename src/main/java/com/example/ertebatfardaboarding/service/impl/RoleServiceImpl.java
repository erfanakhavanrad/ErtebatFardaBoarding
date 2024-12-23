package com.example.ertebatfardaboarding.service.impl;

import com.example.ertebatfardaboarding.ErtebatFardaBoardingApplication;
import com.example.ertebatfardaboarding.domain.Privilege;
import com.example.ertebatfardaboarding.domain.Role;
import com.example.ertebatfardaboarding.domain.dto.RoleDto;
import com.example.ertebatfardaboarding.domain.mapper.RoleMapper;
import com.example.ertebatfardaboarding.domain.responseDto.RoleResponseDto;
import com.example.ertebatfardaboarding.exception.PrivilegeException;
import com.example.ertebatfardaboarding.exception.RoleException;
import com.example.ertebatfardaboarding.repo.PrivilegeRepository;
import com.example.ertebatfardaboarding.repo.RoleRepository;
import com.example.ertebatfardaboarding.security.SecurityService;
import com.example.ertebatfardaboarding.service.RoleService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PrivilegeRepository privilegeRepository;

    @Resource(name = "faMessageSource")
    private MessageSource faMessageSource;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    SecurityService securityService;

    @Override
    public Page<RoleResponseDto> getRoles(Integer pageNo, Integer perPage) {
        Page<Role> all = roleRepository.findAll(ErtebatFardaBoardingApplication.createPagination(pageNo, perPage));
        return all.map(roleMapper::roleToRoleResponseDto);
    }

    @Override
    public RoleResponseDto getRoleById(Long id) throws Exception {
        Role foundRole = roleRepository.findById(id).orElseThrow(() -> new RoleException(faMessageSource.getMessage("ROLE_NOT_FOUND", null, Locale.ENGLISH)));
        return roleMapper.roleToRoleResponseDto(foundRole);
    }

    @Override
    public RoleResponseDto getRoleByName(String name) throws Exception {
        Role allByName = roleRepository.findAllByName(name);
        return roleMapper.roleToRoleResponseDto(allByName);
    }

    @Override
    public RoleResponseDto createRole(String roleName, Long[] ids, HttpServletRequest httpServletRequest) {
        Role role = new Role();
        List<Privilege> allById = privilegeRepository.findAllById(List.of(ids));
        role.setPrivileges(allById);
        role.setName(roleName);
        Role saved = roleRepository.save(role);
        return roleMapper.roleToRoleResponseDto(saved);
    }

    @Override
    public RoleResponseDto updateRole(RoleDto roleDto, HttpServletRequest httpServletRequest) throws Exception {
        Role role = roleRepository.findById(roleDto.getId()).orElseThrow(
                () -> new PrivilegeException(faMessageSource.getMessage("NOT_FOUND", null, Locale.ENGLISH)));
        roleMapper.updateRoleFromDto(roleDto, role);
        Role saved = roleRepository.save(role);
        return roleMapper.roleToRoleResponseDto(saved);
    }

    @Override
    public List<RoleResponseDto> getRolesBySearch(RoleDto roleDto) {
        Specification<Role> specification = Specification.where(null);

        if (roleDto.getName() != null) {
            specification = hasName(roleDto.getName());
        }
        List<Role> all = roleRepository.findAll(specification);
        return roleMapper.roleListToRoleResponseDtoList(all);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }


    private static Specification<Role> hasName(String name) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                "%" + name.toLowerCase() + "%"));
    }

    private Role getRoleByIdRoleResponseDto(Long id) throws Exception {
        return roleRepository.findById(id).orElseThrow(() -> new RoleException(faMessageSource.getMessage("ROLE_NOT_FOUND", null, Locale.ENGLISH)));
    }

}
