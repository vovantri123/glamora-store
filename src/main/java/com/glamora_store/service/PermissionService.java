package com.glamora_store.service;

import com.glamora_store.dto.request.iam.PermissionCreateRequest;
import com.glamora_store.dto.response.iam.PermissionResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface PermissionService {

  @PreAuthorize("hasRole('ADMIN')")
  PermissionResponse createPermission(PermissionCreateRequest request);

  @PreAuthorize("hasRole('ADMIN')")
  List<PermissionResponse> getAllPermissions();

  @PreAuthorize("hasRole('ADMIN')")
  void deletePermission(String permission);
}
