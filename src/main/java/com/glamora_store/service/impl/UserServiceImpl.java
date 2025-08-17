package com.glamora_store.service.impl;

import com.glamora_store.dto.request.UserCreateRequest;
import com.glamora_store.dto.request.UserRoleUpdateRequest;
import com.glamora_store.dto.request.UserUpdateRequest;
import com.glamora_store.dto.response.PageResponse;
import com.glamora_store.dto.response.UserResponse;
import com.glamora_store.entity.User;
import com.glamora_store.enums.ErrorCode;
import com.glamora_store.entity.Role;
import com.glamora_store.enums.RoleName;
import com.glamora_store.mapper.UserMapper;
import com.glamora_store.repository.RoleRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  private final UserMapper userMapper;

  private final PasswordEncoder passwordEncoder;

  @Override
  public UserResponse createUser(UserCreateRequest request) {

    User user = userMapper.toUser(request);
    user.setIsDeleted(false);
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    Role userRole = roleRepository.findByNameAndIsDeletedFalse(RoleName.USER.name())
      .orElseThrow(() -> ExceptionUtil.notFound(ErrorCode.ROLE_NOT_FOUND));

    user.setRoles(Set.of(userRole));

    return userMapper.toUserResponse(userRepository.save(user));
  }

  @Override
  public UserResponse updateUser(Long id, UserUpdateRequest request) {
    User user =
      userRepository
        .findById(id)
        .orElseThrow(() -> ExceptionUtil.notFound(ErrorCode.USER_NOT_FOUND));

    userMapper.toUser(user, request);

    user.setPassword(passwordEncoder.encode(request.getPassword()));

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
  public PageResponse<UserResponse> searchUsers(
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
    Page<UserResponse> userResponsePage = userPage.map(userMapper::toUserResponse);
    return PageResponse.from(userResponsePage);
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
  public UserResponse activeUser(Long id) {
    User user =
      userRepository
        .findById(id)
        .orElseThrow(() -> ExceptionUtil.notFound(ErrorCode.USER_NOT_FOUND));

    user.setIsDeleted(false);

    userRepository.save(user);

    return userMapper.toUserResponse(user);
  }

  @Override
  public UserResponse getMyInfo() {
    SecurityContext context = SecurityContextHolder.getContext();
    String subClaim = context.getAuthentication().getName();

    User user = userRepository.findByEmailAndIsDeletedFalse(subClaim)
      .orElseThrow(() -> ExceptionUtil.notFound(ErrorCode.USER_NOT_FOUND));

    return userMapper.toUserResponse(user);
  }

  @Override
  public UserResponse updateRolesForUser(Long userId, UserRoleUpdateRequest request) {
    User user = userRepository
      .findById(userId)
      .orElseThrow(() -> ExceptionUtil.notFound(ErrorCode.USER_NOT_FOUND));

    Set<String> requestedRoleNames = request.getRoleNames();
    List<Role> roles = roleRepository.findAllById(requestedRoleNames);

    // Lấy ra danh sách role không tồn tại
    Set<String> foundRoleNames = roles.stream()
      .map(Role::getName)
      .collect(Collectors.toSet());

    Set<String> notFoundRoles = requestedRoleNames.stream()
      .filter(r -> !foundRoleNames.contains(r))
      .collect(Collectors.toSet());

    if (!notFoundRoles.isEmpty()) {
      throw ExceptionUtil.with(
        HttpStatus.NOT_FOUND,
        ErrorCode.ROLES_NOT_FOUND,
        String.join(", ", notFoundRoles)
      );
    }

    user.setRoles(new HashSet<>(roles));

    return userMapper.toUserResponse(userRepository.save(user));
  }

}
