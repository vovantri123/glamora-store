package com.glamora_store.service;

import java.util.List;

import com.glamora_store.dto.request.PermissionCreateRequest;
import com.glamora_store.dto.response.PermissionResponse;

public interface PermissionService {

    PermissionResponse createPermission(PermissionCreateRequest request);

    List<PermissionResponse> getAllPermissions();

    void deletePermission(String permission);
}
