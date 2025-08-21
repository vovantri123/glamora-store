package com.glamora_store.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.glamora_store.dto.request.PermissionCreateRequest;
import com.glamora_store.dto.response.PermissionResponse;

public interface PermissionService {

    @PreAuthorize("hasRole('ADMIN')")
    PermissionResponse createPermission(PermissionCreateRequest request);

    @PreAuthorize("hasRole('ADMIN')")
    List<PermissionResponse> getAllPermissions();

    @PreAuthorize("hasRole('ADMIN')")
    void deletePermission(String permission);
}
