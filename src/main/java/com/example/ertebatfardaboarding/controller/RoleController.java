package com.example.ertebatfardaboarding.controller;

import com.example.ertebatfardaboarding.domain.ResponseModel;
import com.example.ertebatfardaboarding.domain.dto.RoleDto;
import com.example.ertebatfardaboarding.domain.responseDto.RoleResponseDto;
import com.example.ertebatfardaboarding.service.RoleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("role")
@Slf4j
public class RoleController {

    ResponseModel responseModel = new ResponseModel();

    @Autowired
    RoleService roleService;

    @GetMapping("/getAll")
    public ResponseModel getAll(@RequestParam Integer pageNo, Integer perPage, HttpServletResponse httpServletResponse) throws Exception {
        log.info("Get all Roles");
        responseModel.clear();
        Page<RoleResponseDto> roles = roleService.getRoles(pageNo, perPage);
        responseModel.setContents(roles.getContent());
        responseModel.setRecordCount((int) roles.getTotalElements());
        responseModel.setStatus(httpServletResponse.getStatus());
        return responseModel;
    }

    @GetMapping(path = "/getById")
    public ResponseModel getById(@RequestParam Long id, HttpServletResponse httpServletResponse) throws Exception {
        log.info("Get Role ById");
        responseModel.clear();
        responseModel.setContent(roleService.getRoleById(id));
        responseModel.setRecordCount(1);
        responseModel.setStatus(httpServletResponse.getStatus());
        return responseModel;
    }

    @GetMapping("/searchRole")
    public ResponseModel searchRole(@RequestBody RoleDto roleDto, HttpServletResponse httpServletResponse) {
        responseModel.clear();
        List<RoleResponseDto> roles = roleService.getRolesBySearch(roleDto);
        responseModel.setContents(roles);
        responseModel.setRecordCount((int) roles.size());
        responseModel.setStatus(httpServletResponse.getStatus());
        return responseModel;
    }

    @PostMapping("/save")
    public ResponseModel save(@RequestParam String roleName, @RequestBody Long[] ids, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        log.info("Save Role");
        responseModel.clear();
        RoleResponseDto roleResponseDto = roleService.createRole(roleName, ids, httpServletRequest);
        responseModel.setContent(roleResponseDto);
        responseModel.setStatus(httpServletResponse.getStatus());
        return responseModel;
    }

    @PutMapping(path = "/update")
    public ResponseModel update(@RequestBody RoleDto roleDto, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        log.info("update Role");
        responseModel.clear();
        responseModel.setContent(roleService.updateRole(roleDto, httpServletRequest));
        responseModel.setStatus(httpServletResponse.getStatus());
        return responseModel;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseModel delete(@PathVariable("id") Long id, HttpServletResponse httpServletResponse) throws Exception {
        log.info("delete Role");
        responseModel.clear();
        roleService.deleteRole(id);
        responseModel.setStatus(httpServletResponse.getStatus());
        responseModel.clear();
        return responseModel;
    }

}
