package com.glamora_store.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.glamora_store.dto.request.iam.RoleCreateRequest;
import com.glamora_store.dto.response.iam.RoleResponse;

public interface RoleService {

    @PreAuthorize("hasRole('ADMIN')")
    RoleResponse createRole(RoleCreateRequest request);

    @PreAuthorize("hasRole('ADMIN')")
    List<RoleResponse> getAllRoles();

    @PreAuthorize("hasRole('ADMIN')")
    void deleteRole(String role);
}
