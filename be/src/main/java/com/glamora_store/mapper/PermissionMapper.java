package com.glamora_store.mapper;

import com.glamora_store.dto.request.admin.iam.PermissionCreateRequest;
import com.glamora_store.dto.response.admin.iam.PermissionResponse;
import com.glamora_store.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PermissionMapper {
  Permission toPermission(PermissionCreateRequest request);

  PermissionResponse toPermissionResponse(Permission permission);
}
