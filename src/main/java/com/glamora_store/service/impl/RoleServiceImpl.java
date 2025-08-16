package com.glamora_store.service.impl;

import com.glamora_store.dto.request.RoleCreateRequest;
import com.glamora_store.dto.response.RoleResponse;
import com.glamora_store.entity.Permission;
import com.glamora_store.entity.Role;
import com.glamora_store.enums.ErrorCode;
import com.glamora_store.mapper.RoleMapper;
import com.glamora_store.repository.PermissionRepository;
import com.glamora_store.repository.RoleRepository;
import com.glamora_store.service.RoleService;
import com.glamora_store.util.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
  private final RoleRepository roleRepository;
  private final PermissionRepository permissionRepository;
  private final RoleMapper roleMapper;

  @Override
  public RoleResponse create(RoleCreateRequest request) {
    Role role = roleMapper.toRole(request);

    // Lấy danh sách quyền từ DB
    List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());

    // Nếu thiếu permission => quăng exception
    if (permissions.size() != request.getPermissions().size()) {
      throw ExceptionUtil.notFound(ErrorCode.PERMISSION_NOT_FOUND);
    }

    role.setPermissions(new HashSet<>(permissions));
    role.setIsDeleted(false);

    role = roleRepository.save(role);
    return roleMapper.toRoleResponse(role);
  }

  @Override
  public List<RoleResponse> getAll() {
    return roleRepository.findAll()
      .stream()
      .map(roleMapper::toRoleResponse)
      .toList();
  }

  @Override
  public void delete(String role) {
    roleRepository.deleteById(role);
  }
}
