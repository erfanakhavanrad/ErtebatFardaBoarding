package com.example.ertebatfardaboarding.service;

import com.example.ertebatfardaboarding.domain.Privilege;
import com.example.ertebatfardaboarding.domain.dto.PrivilegeDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

public interface PrivilegeService {
    Page<Privilege> getPrivileges(Integer pageNo, Integer perPage);
    Privilege getPrivilegeById(Long id) throws Exception;
    Privilege createPrivilege(PrivilegeDto privilegeDto, HttpServletRequest httpServletRequest) throws Exception;
    Privilege updatePrivilege(PrivilegeDto privilegeDto, HttpServletRequest httpServletRequest) throws Exception;
    void deletePrivilege(Long id);
}
