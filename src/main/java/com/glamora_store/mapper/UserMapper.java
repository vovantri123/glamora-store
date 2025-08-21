package com.glamora_store.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.glamora_store.dto.request.UserCreateRequest;
import com.glamora_store.dto.request.UserProfileUpdateRequest;
import com.glamora_store.dto.request.UserUpdateRequest;
import com.glamora_store.dto.response.UserProfileResponse;
import com.glamora_store.dto.response.UserResponse;
import com.glamora_store.entity.User;

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
