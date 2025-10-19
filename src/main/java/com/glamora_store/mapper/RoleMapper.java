package com.glamora_store.mapper;

import com.glamora_store.dto.request.iam.RoleCreateRequest;
import com.glamora_store.dto.response.iam.RoleResponse;
import com.glamora_store.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
  @Mapping(target = "permissions", ignore = true)
  Role toRole(RoleCreateRequest request);

  RoleResponse toRoleResponse(Role role);
}
