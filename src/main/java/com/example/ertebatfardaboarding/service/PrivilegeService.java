package com.example.ertebatfardaboarding.service;

import com.example.ertebatfardaboarding.domain.dto.PrivilegeDto;
import com.example.ertebatfardaboarding.domain.responseDto.PrivilegeResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

public interface PrivilegeService {
    Page<PrivilegeResponseDto> getPrivileges(Integer pageNo, Integer perPage);

    PrivilegeResponseDto getPrivilegeById(Long id) throws Exception;

    PrivilegeResponseDto createPrivilege(PrivilegeDto privilegeDto, HttpServletRequest httpServletRequest) throws Exception;

    PrivilegeResponseDto updatePrivilege(PrivilegeDto privilegeDto, HttpServletRequest httpServletRequest) throws Exception;

    void deletePrivilege(Long id);
}
