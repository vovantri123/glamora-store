package com.glamora_store.service.impl;

import com.glamora_store.dto.request.UserCreationRequest;
import com.glamora_store.dto.request.UserUpdateRequest;
import com.glamora_store.dto.response.UserResponse;
import com.glamora_store.entity.User;
import com.glamora_store.enums.ErrorCode;
import com.glamora_store.mapper.UserMapper;
import com.glamora_store.repository.UserRepository;
import com.glamora_store.service.UserService;
import com.glamora_store.util.ExceptionUtil;
import com.glamora_store.util.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public UserResponse createUser(UserCreationRequest request) {
    User user = userMapper.toUser(request);
    user.setIsDeleted(false);
    return userMapper.toUserResponse(userRepository.save(user));
  }

  @Override
  public UserResponse updateUser(Long id, UserUpdateRequest request) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> ExceptionUtil.notFound(ErrorCode.USER_NOT_FOUND));

    userMapper.toUser(user, request);
    return userMapper.toUserResponse(userRepository.save(user));
  }

  @Override
  public void softDeleteUser(Long id) {
    User user =
        userRepository
            .findByUserIdAndIsDeletedFalse(id)
            .orElseThrow(() -> ExceptionUtil.notFound(ErrorCode.USER_NOT_FOUND));

    user.setIsDeleted(true);

    userRepository.save(user);
  }

  @Override
  public Page<UserResponse> searchUsers(
      String fullname, LocalDate dob, int page, int size, String sortBy, String sortDir) {
    Specification<User> spec =
        UserSpecification.isNotDeleted()
            .and(UserSpecification.hasFullNameLike(fullname))
            .and(UserSpecification.hasDobEqual(dob));

    Sort sort =
        sortDir.equalsIgnoreCase("asc")
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();

    Pageable pageable = PageRequest.of(page, size, sort);
    Page<User> userPage = userRepository.findAll(spec, pageable);
    return userPage.map(userMapper::toUserResponse);
  }

  @Override
  public UserResponse getUserById(Long id) {
    User user =
        userRepository
            .findByUserIdAndIsDeletedFalse(id)
            .orElseThrow(() -> ExceptionUtil.notFound(ErrorCode.USER_NOT_FOUND));
    return userMapper.toUserResponse(user);
  }

  @Override
  public UserResponse activeUser(Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> ExceptionUtil.notFound(ErrorCode.USER_NOT_FOUND));

    user.setIsDeleted(false);

    userRepository.save(user);

    return userMapper.toUserResponse(user);
  }
}
