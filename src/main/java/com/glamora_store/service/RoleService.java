package com.glamora_store.service;

import com.glamora_store.dto.request.RoleCreateRequest;
import com.glamora_store.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {

  RoleResponse create(RoleCreateRequest request);

  List<RoleResponse> getAll();

  void delete(String role);
}
