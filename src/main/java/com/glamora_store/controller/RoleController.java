package com.glamora_store.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.glamora_store.dto.request.RoleCreateRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.RoleResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.RoleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Slf4j
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleCreateRequest request) {
        return new ApiResponse<>(SuccessMessage.CREATE_ROLE_SUCCESS.getMessage(), roleService.createRole(request));
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRoles() {
        return new ApiResponse<>(SuccessMessage.GET_ALL_ROLE_SUCCESS.getMessage(), roleService.getAllRoles());
    }

    @DeleteMapping("/{role}")
    ApiResponse<Void> deleteRole(@PathVariable String role) {
        roleService.deleteRole(role);
        return new ApiResponse<>(SuccessMessage.DELETE_ROLE_SUCCESS.getMessage());
    }
}
