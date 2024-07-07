package com.example.ertebatfardaboarding.service.impl;

import com.example.ertebatfardaboarding.ErtebatFardaBoardingApplication;
import com.example.ertebatfardaboarding.domain.Privilege;
import com.example.ertebatfardaboarding.domain.dto.PrivilegeDto;
import com.example.ertebatfardaboarding.domain.mapper.PrivilegeMapper;
import com.example.ertebatfardaboarding.domain.responseDto.PrivilegeResponseDto;
import com.example.ertebatfardaboarding.exception.PrivilegeException;
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

    @Autowired
    PrivilegeMapper privilegeMapper;

    @Resource(name = "faMessageSource")
    private MessageSource faMessageSource;

    @Override
    public Page<PrivilegeResponseDto> getPrivileges(Integer pageNo, Integer perPage) {
        Page<Privilege> all = privilegeRepository.findAll(ErtebatFardaBoardingApplication.createPagination(pageNo, perPage));
        return all.map(privilegeMapper::privilegeToPrivilegeResponseDto);
    }

    @Override
    public PrivilegeResponseDto getPrivilegeById(Long id) throws Exception {
        Privilege privilege = privilegeRepository.findById(id).orElseThrow(() -> new PrivilegeException(faMessageSource.getMessage("PRIVILEGE_NOT_FOUND", null, Locale.ENGLISH)));
        return privilegeMapper.privilegeToPrivilegeResponseDto(privilege);
    }

    @Override
    public PrivilegeResponseDto createPrivilege(PrivilegeDto privilegeDto, HttpServletRequest httpServletRequest) {
        Privilege privilege = PrivilegeMapper.privilegeMapper.privilegeDtoToPrivilege(privilegeDto);
        Privilege saved = privilegeRepository.save(privilege);
        return privilegeMapper.privilegeToPrivilegeResponseDto(saved);
    }

    @Override
    public PrivilegeResponseDto updatePrivilege(PrivilegeDto privilegeDto, HttpServletRequest httpServletRequest) throws Exception {
        Privilege privilege = privilegeRepository.findById(privilegeDto.getId()).orElseThrow(
                () -> new PrivilegeException(faMessageSource.getMessage("NOT_FOUND", null, Locale.ENGLISH)));
        privilegeMapper.updatePrivilegeFromDto(privilegeDto, privilege);
        Privilege saved = privilegeRepository.save(privilege);
        return privilegeMapper.privilegeToPrivilegeResponseDto(saved);
    }

    @Override
    public void deletePrivilege(Long id) {
        privilegeRepository.deleteById(id);
    }


    private Privilege getPrivilegeByIdBase(Long id) throws Exception {
        return privilegeRepository.findById(id).orElseThrow(() -> new PrivilegeException(faMessageSource.getMessage("PRIVILEGE_NOT_FOUND", null, Locale.ENGLISH)));
    }

}
