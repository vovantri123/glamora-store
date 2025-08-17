package com.glamora_store.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.glamora_store.dto.request.PermissionCreateRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.PermissionResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.PermissionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
                permissionService.createPermission(request));
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAllPermissions() {
        return new ApiResponse<>(
                HttpStatus.OK.value(),
                SuccessMessage.GET_ALL_PERMISSION_SUCCESS.getMessage(),
                permissionService.getAllPermissions());
    }

    @DeleteMapping("/{permission}")
    ApiResponse<Void> deletePermission(@PathVariable String permission) {
        permissionService.deletePermission(permission);
        return new ApiResponse<>(HttpStatus.OK.value(), SuccessMessage.DELETE_PERMISSION_SUCCESS.getMessage());
    }
}
