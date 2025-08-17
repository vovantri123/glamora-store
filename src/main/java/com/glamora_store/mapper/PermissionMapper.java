package com.glamora_store.mapper;

import org.mapstruct.Mapper;

import com.glamora_store.dto.request.PermissionCreateRequest;
import com.glamora_store.dto.response.PermissionResponse;
import com.glamora_store.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionCreateRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
