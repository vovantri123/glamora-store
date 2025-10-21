package com.glamora_store.controller.admin;

import com.glamora_store.dto.request.admin.iam.RoleCreateRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.admin.iam.RoleResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
public class RoleController {
  private final RoleService roleService;

  @PostMapping
  public ApiResponse<RoleResponse> createRole(@Valid @RequestBody RoleCreateRequest request) {
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
