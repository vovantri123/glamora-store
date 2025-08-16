package com.glamora_store.controller;

import com.glamora_store.dto.request.RoleCreateRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.RoleResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Slf4j
public class RoleController {
  private final RoleService roleService;

  @PostMapping
  public ApiResponse<RoleResponse> createRole(@RequestBody RoleCreateRequest request) {
    return new ApiResponse<>(
      HttpStatus.CREATED.value(),
      SuccessMessage.CREATE_ROLE_SUCCESS.getMessage(),
      roleService.create(request)
    );
  }

  @GetMapping
  public ApiResponse<List<RoleResponse>> getAllRole() {
    return new ApiResponse<>(
      HttpStatus.OK.value(),
      SuccessMessage.GET_ALL_ROLE_SUCCESS.getMessage(),
      roleService.getAll()
    );
  }

  @DeleteMapping("/{role}")
  ApiResponse<Void> deleteRole(@PathVariable String role) {
    roleService.delete(role);
    return new ApiResponse<>(
      HttpStatus.OK.value(),
      SuccessMessage.DELETE_ROLE_SUCCESS.getMessage()
    );
  }
}
