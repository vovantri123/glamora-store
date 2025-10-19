package com.glamora_store.mapper;

import com.glamora_store.dto.request.iam.PermissionCreateRequest;
import com.glamora_store.dto.response.iam.PermissionResponse;
import com.glamora_store.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
  Permission toPermission(PermissionCreateRequest request);

  PermissionResponse toPermissionResponse(Permission permission);
}
