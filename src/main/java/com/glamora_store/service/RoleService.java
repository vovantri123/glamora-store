package com.glamora_store.service;

import com.glamora_store.dto.request.admin.iam.RoleCreateRequest;
import com.glamora_store.dto.response.admin.iam.RoleResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface RoleService {

  @PreAuthorize("hasRole('ADMIN')")
  RoleResponse createRole(RoleCreateRequest request);

  @PreAuthorize("hasRole('ADMIN')")
  List<RoleResponse> getAllRoles();

  @PreAuthorize("hasRole('ADMIN')")
  void deleteRole(String role);
}
