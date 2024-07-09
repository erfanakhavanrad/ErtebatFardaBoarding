package com.example.ertebatfardaboarding.controller;

import com.example.ertebatfardaboarding.domain.ResponseModel;
import com.example.ertebatfardaboarding.domain.dto.PrivilegeDto;
import com.example.ertebatfardaboarding.domain.responseDto.PrivilegeResponseDto;
import com.example.ertebatfardaboarding.service.PrivilegeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("privilege")
@Slf4j
public class PrivilegeController {

    ResponseModel responseModel = new ResponseModel();

    @Autowired
    PrivilegeService privilegeService;

    @GetMapping("/getAll")
    public ResponseModel getAll(@RequestParam Integer pageNo, Integer perPage, HttpServletResponse httpServletResponse) {
        log.info("getAll Privilege");
        responseModel.clear();
        Page<PrivilegeResponseDto> privileges = privilegeService.getPrivileges(pageNo, perPage);
        responseModel.setContents(privileges.getContent());
        responseModel.setRecordCount((int) privileges.getTotalElements());
        responseModel.setStatus(httpServletResponse.getStatus());
        return responseModel;
    }

    @GetMapping(path = "/getById")
    public ResponseModel getById(@RequestParam Long id, HttpServletResponse httpServletResponse) throws Exception {
        log.info("getById Privilege");
        responseModel.clear();
        responseModel.setContent(privilegeService.getPrivilegeById(id));
        responseModel.setStatus(httpServletResponse.getStatus());
        return responseModel;
    }

    @PostMapping("/save")
    public ResponseModel save(@RequestBody PrivilegeDto privilegeDto, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        log.info("save Privilege");
        responseModel.clear();
        responseModel.setContent(privilegeService.createPrivilege(privilegeDto, httpServletRequest));
        responseModel.setStatus(httpServletResponse.getStatus());
        return responseModel;
    }

    @PutMapping(path = "/update")
    public ResponseModel update(@RequestBody PrivilegeDto privilegeDto, HttpServletRequest httpServletRequest) throws Exception {
        log.info("update Privilege");
        responseModel.clear();
        responseModel.setContent(privilegeService.updatePrivilege(privilegeDto, httpServletRequest));
        return responseModel;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseModel delete(@PathVariable("id") Long id) {
        log.info("delete Privilege");
        responseModel.clear();
        privilegeService.deletePrivilege(id);
        responseModel.clear();
        return responseModel;
    }

}
