package com.glamora_store.mapper;

import com.glamora_store.dto.request.UserCreationRequest;
import com.glamora_store.dto.request.UserUpdateRequest;
import com.glamora_store.dto.response.UserResponse;
import com.glamora_store.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring") // Có thể mở trong target (sau khi run) để xem nó tạo @Mapper map dữ liệu thế nào
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    List<UserResponse> toListUserResponse(List<User> users);

    void updateUser(@MappingTarget User user, UserUpdateRequest request); // @MappingTarget giúp cập nhật trực tiếp UserUpdateRequest vào User có sẵn (bởi sẽ có những trường mà target có nhưng source không có, phù hợp cho việc update)
}