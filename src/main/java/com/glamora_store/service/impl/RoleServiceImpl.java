package com.glamora_store.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.glamora_store.dto.request.iam.RoleCreateRequest;
import com.glamora_store.dto.response.iam.RoleResponse;
import com.glamora_store.entity.Permission;
import com.glamora_store.entity.Role;
import com.glamora_store.enums.ErrorMessage;
import com.glamora_store.mapper.RoleMapper;
import com.glamora_store.repository.PermissionRepository;
import com.glamora_store.repository.RoleRepository;
import com.glamora_store.service.RoleService;
import com.glamora_store.util.ExceptionUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleResponse createRole(RoleCreateRequest request) {
        Role role = roleMapper.toRole(request);

        // Lấy danh sách quyền từ DB
        Set<String> requestedPermission = request.getPermissions();
        List<Permission> permissions = permissionRepository.findAllById(requestedPermission);

        Set<String> foundPermissions =
                permissions.stream().map(Permission::getName).collect(Collectors.toSet());

        Set<String> notFoundPermissions = requestedPermission.stream()
                .filter(r -> !foundPermissions.contains(r))
                .collect(Collectors.toSet());

        if (!notFoundPermissions.isEmpty()) {
            throw ExceptionUtil.with(
                    HttpStatus.NOT_FOUND, ErrorMessage.PERMISSIONS_NOT_FOUND, String.join(", ", notFoundPermissions));
        }

        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    @Override
    public void deleteRole(String role) {
        roleRepository.deleteById(role);
    }
}
