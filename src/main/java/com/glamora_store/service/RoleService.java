package com.glamora_store.service;

import java.util.List;

import com.glamora_store.dto.request.RoleCreateRequest;
import com.glamora_store.dto.response.RoleResponse;

public interface RoleService {

    RoleResponse createRole(RoleCreateRequest request);

    List<RoleResponse> getAllRoles();

    void deleteRole(String role);
}
