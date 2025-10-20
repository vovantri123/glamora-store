package com.glamora_store.controller;

import com.glamora_store.dto.request.iam.RoleCreateRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.iam.RoleResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Slf4j
public class RoleController {
  private final RoleService roleService;

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<RoleResponse> createRole(@Valid @RequestBody RoleCreateRequest request) {
    return new ApiResponse<>(SuccessMessage.CREATE_ROLE_SUCCESS.getMessage(), roleService.createRole(request));
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<RoleResponse>> getAllRoles() {
    return new ApiResponse<>(SuccessMessage.GET_ALL_ROLE_SUCCESS.getMessage(), roleService.getAllRoles());
  }

  @DeleteMapping("/{role}")
  @PreAuthorize("hasRole('ADMIN')")
  ApiResponse<Void> deleteRole(@PathVariable String role) {
    roleService.deleteRole(role);
    return new ApiResponse<>(SuccessMessage.DELETE_ROLE_SUCCESS.getMessage());
  }
}
