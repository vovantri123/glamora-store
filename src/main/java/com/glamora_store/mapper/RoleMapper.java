package com.glamora_store.mapper;

import com.glamora_store.dto.request.admin.iam.RoleCreateRequest;
import com.glamora_store.dto.response.admin.iam.RoleResponse;
import com.glamora_store.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {
  @Mapping(target = "permissions", ignore = true)
  Role toRole(RoleCreateRequest request);

  RoleResponse toRoleResponse(Role role);
}
