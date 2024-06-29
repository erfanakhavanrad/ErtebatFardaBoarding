package com.example.ertebatfardaboarding.service.impl;

import com.example.ertebatfardaboarding.ErtebatFardaBoardingApplication;
import com.example.ertebatfardaboarding.domain.Privilege;
import com.example.ertebatfardaboarding.domain.ResponseModel;
import com.example.ertebatfardaboarding.domain.Role;
import com.example.ertebatfardaboarding.domain.dto.RoleDto;
import com.example.ertebatfardaboarding.domain.mapper.RoleMapper;
import com.example.ertebatfardaboarding.repo.PrivilegeRepository;
import com.example.ertebatfardaboarding.repo.RoleRepository;
import com.example.ertebatfardaboarding.security.SecurityService;
import com.example.ertebatfardaboarding.service.RoleService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    ResponseModel responseModel;

    @Autowired
    SecurityService securityService;

    @Override
    public Page<Role> getRoles(Integer pageNo, Integer perPage) {
        return roleRepository.findAll(ErtebatFardaBoardingApplication.createPagination(pageNo, perPage));
    }

    @Override
    public Role getRoleById(Long id) throws Exception {
        return roleRepository.findById(id).orElseThrow(() -> new Exception(faMessageSource.getMessage("ROLE_NOT_FOUND", null, Locale.ENGLISH)));
    }

    @Override
    public Role getRoleByName(String name) throws Exception {
        return roleRepository.findAllByName(name);
    }

    @Override
    public Role createRole(RoleDto roleDto, HttpServletRequest httpServletRequest) {
        Role role = RoleMapper.roleMapper.roleDtoToRole(roleDto);
        List<Long> ids = new ArrayList<>();
        for (Privilege privilege : roleDto.getPrivileges()) {
            ids.add(privilege.getId());
        }
        List<Privilege> allById = privilegeRepository.findAllById(ids);
        role.setPrivileges(allById);
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(RoleDto roleDto, HttpServletRequest httpServletRequest) throws Exception {

        Role oldRole = getRoleById(roleDto.getId());
        Role newRole = RoleMapper.roleMapper.roleDtoToRole(roleDto);

        responseModel.clear();
        Role updated = (Role) responseModel.merge(oldRole, newRole);

        if (roleDto.getPrivileges() != null && !roleDto.getPrivileges().isEmpty()) {
            updated.setPrivileges(newRole.getPrivileges());
        }

        return roleRepository.save(updated);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
