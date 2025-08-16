package com.glamora_store.service;

import com.glamora_store.dto.request.PermissionCreateRequest;
import com.glamora_store.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {

  PermissionResponse createPermission(PermissionCreateRequest request);

  List<PermissionResponse> getAllPermissions();

  void deletePermission(String permission);
}
