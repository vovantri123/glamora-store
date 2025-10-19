package com.glamora_store.controller;

import com.glamora_store.dto.request.iam.PermissionCreateRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.iam.PermissionResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@Slf4j
public class PermissionController {
  private final PermissionService permissionService;

  @PostMapping
  ApiResponse<PermissionResponse> createPermission(@Valid @RequestBody PermissionCreateRequest request) {
    return new ApiResponse<>(
      SuccessMessage.CREATE_PERMISSION_SUCCESS.getMessage(), permissionService.createPermission(request));
  }

  @GetMapping
  ApiResponse<List<PermissionResponse>> getAllPermissions() {
    return new ApiResponse<>(
      SuccessMessage.GET_ALL_PERMISSION_SUCCESS.getMessage(), permissionService.getAllPermissions());
  }

  @DeleteMapping("/{permission}")
  ApiResponse<Void> deletePermission(@PathVariable String permission) {
    permissionService.deletePermission(permission);
    return new ApiResponse<>(SuccessMessage.DELETE_PERMISSION_SUCCESS.getMessage());
  }
}
