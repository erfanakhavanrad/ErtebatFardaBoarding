package com.example.ertebatfardaboarding.service.impl;

import com.example.ertebatfardaboarding.ErtebatFardaBoardingApplication;
import com.example.ertebatfardaboarding.domain.Privilege;
import com.example.ertebatfardaboarding.domain.ResponseModel;
import com.example.ertebatfardaboarding.domain.dto.PrivilegeDto;
import com.example.ertebatfardaboarding.domain.mapper.PrivilegeMapper;
import com.example.ertebatfardaboarding.repo.PrivilegeRepository;
import com.example.ertebatfardaboarding.service.PrivilegeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {
    @Autowired
    PrivilegeRepository privilegeRepository;

    @Resource(name = "faMessageSource")
    private MessageSource faMessageSource;

    @Autowired
    ResponseModel responseModel;

    @Override
    public Page<Privilege> getPrivileges(Integer pageNo, Integer perPage) {
        return privilegeRepository.findAll(ErtebatFardaBoardingApplication.createPagination(pageNo, perPage));
    }

    @Override
    public Privilege getPrivilegeById(Long id) throws Exception {
        return privilegeRepository.findById(id).orElseThrow(() -> new Exception(faMessageSource.getMessage("PRIVILEGE_NOT_FOUND", null, Locale.ENGLISH)));
    }

    @Override
    public Privilege createPrivilege(PrivilegeDto privilegeDto, HttpServletRequest httpServletRequest) {
        Privilege privilege = PrivilegeMapper.privilegeMapper.privilegeDtoToPrivilege(privilegeDto);
        return privilegeRepository.save(privilege);
    }

    @Override
    public Privilege updatePrivilege(PrivilegeDto privilegeDto, HttpServletRequest httpServletRequest) throws Exception {

        Privilege oldPrivilege = getPrivilegeById(privilegeDto.getId());
        Privilege newPrivilege = PrivilegeMapper.privilegeMapper.privilegeDtoToPrivilege(privilegeDto);

        responseModel.clear();
        Privilege updated = (Privilege) responseModel.merge(oldPrivilege, newPrivilege);
        return privilegeRepository.save(updated);
    }

    @Override
    public void deletePrivilege(Long id) {
        privilegeRepository.deleteById(id);
    }
}
