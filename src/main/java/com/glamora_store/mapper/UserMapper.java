package com.glamora_store.mapper;

import com.glamora_store.dto.request.iam.UserCreateRequest;
import com.glamora_store.dto.request.iam.UserProfileUpdateRequest;
import com.glamora_store.dto.request.iam.UserUpdateRequest;
import com.glamora_store.dto.response.iam.UserProfileResponse;
import com.glamora_store.dto.response.iam.UserResponse;
import com.glamora_store.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
  User toUser(UserCreateRequest request);

  UserResponse toUserResponse(User user);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void toUser(@MappingTarget User user, UserProfileUpdateRequest request);

  UserProfileResponse toUserProfileResponse(User user);

  List<UserResponse> toListUserResponse(List<User> users);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void toUser(@MappingTarget User user, UserUpdateRequest request);
}
