package com.glamora_store.mapper;

import com.glamora_store.dto.request.admin.iam.UserCreateRequest;
import com.glamora_store.dto.request.admin.iam.UserUpdateRequest;
import com.glamora_store.dto.request.user.iam.UserProfileUpdateRequest;
import com.glamora_store.dto.response.admin.iam.UserResponse;
import com.glamora_store.dto.response.user.iam.UserProfileResponse;
import com.glamora_store.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
  User toUser(UserCreateRequest request);

  UserResponse toUserResponse(User user);

  void toUser(@MappingTarget User user, UserProfileUpdateRequest request);

  UserProfileResponse toUserProfileResponse(User user);

  List<UserResponse> toListUserResponse(List<User> users);

  void toUser(@MappingTarget User user, UserUpdateRequest request);
}
