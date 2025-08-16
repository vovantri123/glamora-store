package com.glamora_store.service.impl;

import com.glamora_store.dto.request.PermissionCreateRequest;
import com.glamora_store.dto.response.PermissionResponse;
import com.glamora_store.entity.Permission;
import com.glamora_store.mapper.PermissionMapper;
import com.glamora_store.repository.PermissionRepository;
import com.glamora_store.service.PermissionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionServiceImpl implements PermissionService {
  private final PermissionRepository permissionRepository;
  private final PermissionMapper permissionMapper;

  @Override
  public PermissionResponse createPermission(PermissionCreateRequest request) {
    Permission permission = permissionMapper.toPermission(request);
    return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
  }

  @Override
  public List<PermissionResponse> getAllPermissions() {
    List<Permission> permissions = permissionRepository.findAll();
    return permissions.stream()
      .map(permissionMapper::toPermissionResponse)
      .toList();
  }

  @Override
  public void deletePermission(String permission) {
    permissionRepository.deleteById(permission);
  }
}
