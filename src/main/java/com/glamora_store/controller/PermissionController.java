package com.glamora_store.controller;

import com.glamora_store.dto.request.PermissionCreateRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.PermissionResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@Slf4j
public class PermissionController {
  private final PermissionService permissionService;

  @PostMapping
  ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionCreateRequest request) {
    return new ApiResponse<>(
      HttpStatus.CREATED.value(),
      SuccessMessage.CREATE_PERMISSION_SUCCESS.getMessage(),
      permissionService.create(request)
    );
  }

  @GetMapping
  ApiResponse<List<PermissionResponse>> getAllPermission() {
    return new ApiResponse<>(
      HttpStatus.OK.value(),
      SuccessMessage.GET_ALL_PERMISSION_SUCCESS.getMessage(),
      permissionService.getAll()
    );
  }

  @DeleteMapping("/{permission}")
  ApiResponse<Void> deletePermission(@PathVariable String permission) {
    permissionService.delete(permission);
    return new ApiResponse<>(
      HttpStatus.OK.value(),
      SuccessMessage.DELETE_PERMISSION_SUCCESS.getMessage()
    );
  }
}
