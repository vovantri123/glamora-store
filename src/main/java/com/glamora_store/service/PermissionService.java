package com.glamora_store.service;

import com.glamora_store.dto.request.iam.PermissionCreateRequest;
import com.glamora_store.dto.response.iam.PermissionResponse;

import java.util.List;

public interface PermissionService {

  PermissionResponse createPermission(PermissionCreateRequest request);

  List<PermissionResponse> getAllPermissions();

  void deletePermission(String permission);
}
