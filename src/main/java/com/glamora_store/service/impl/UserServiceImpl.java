package com.glamora_store.service.impl;

import com.glamora_store.dto.request.admin.iam.UserCreateRequest;
import com.glamora_store.dto.request.admin.iam.UserRoleUpdateRequest;
import com.glamora_store.dto.request.admin.iam.UserUpdateRequest;
import com.glamora_store.dto.request.common.iam.PasswordUpdateRequest;
import com.glamora_store.dto.request.user.iam.UserProfileUpdateRequest;
import com.glamora_store.dto.response.admin.iam.UserResponse;
import com.glamora_store.dto.response.common.PageResponse;
import com.glamora_store.dto.response.user.iam.UserProfileResponse;
import com.glamora_store.entity.Role;
import com.glamora_store.entity.User;
import com.glamora_store.enums.ErrorMessage;
import com.glamora_store.enums.OtpPurpose;
import com.glamora_store.enums.RoleName;
import com.glamora_store.mapper.UserMapper;
import com.glamora_store.repository.RoleRepository;
import com.glamora_store.repository.UserRepository;
import com.glamora_store.service.OtpEmailService;
import com.glamora_store.service.UserService;
import com.glamora_store.util.ExceptionUtil;
import com.glamora_store.util.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  private final UserMapper userMapper;

  private final PasswordEncoder passwordEncoder;

  private final OtpEmailService otpEmailService;

  @Override
  public void registerUser(UserCreateRequest request) {
    Optional<User> existingUserOpt = userRepository.findByEmail(request.getEmail());

    if (existingUserOpt.isPresent()) {
      User existingUser = existingUserOpt.get();
      if (Boolean.FALSE.equals(existingUser.getIsDeleted())) {
        throw ExceptionUtil.badRequest(ErrorMessage.USER_EXISTED);
      }

      // user chưa active → gửi OTP mới
      otpEmailService.sendOtp(request.getEmail(), OtpPurpose.REGISTER);
    }

    // user chưa tồn tại → tạo mới
    User user = userMapper.toUser(request);
    user.setAvatar(
      "https://img.freepik.com/premium-vector/profile-picture-placeholder-avatar-silhouette-gray-tones-icon-colored-shapes-gradient_1076610-40164.jpg?w=360");
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setIsDeleted(true);

    Role userRole = roleRepository
      .findById(RoleName.USER.name())
      .orElseThrow(() -> ExceptionUtil.notFound(ErrorMessage.ROLE_NOT_FOUND));

    user.setRoles(Set.of(userRole));
    userRepository.save(user);

    // gửi OTP lần đầu
    otpEmailService.sendOtp(request.getEmail(), OtpPurpose.REGISTER);
  }

  @Override
  public UserResponse createUser(UserCreateRequest request) {
    Optional<User> existingUserOpt = userRepository.findByEmail(request.getEmail());

    if (existingUserOpt.isPresent()) {
      User existingUser = existingUserOpt.get();
      if (Boolean.FALSE.equals(existingUser.getIsDeleted())) {
        throw ExceptionUtil.badRequest(ErrorMessage.USER_EXISTED);
      }

      activeUser(existingUser.getId());
    }

    // user chưa tồn tại → tạo mới
    User user = userMapper.toUser(request);
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setIsDeleted(false);

    Role userRole = roleRepository
      .findById(RoleName.USER.name())
      .orElseThrow(() -> ExceptionUtil.notFound(ErrorMessage.ROLE_NOT_FOUND));

    user.setRoles(Set.of(userRole));

    return userMapper.toUserResponse(userRepository.save(user));
  }

  @Override
  public UserResponse updateUser(Long id, UserUpdateRequest request) {
    User user = userRepository
      .findByIdAndIsDeletedFalse(id)
      .orElseThrow(() -> ExceptionUtil.notFound(ErrorMessage.USER_NOT_FOUND));

    userMapper.toUser(user, request);

    return userMapper.toUserResponse(userRepository.save(user));
  }

  @Override
  public UserProfileResponse updateMyProfile(Long userId, UserProfileUpdateRequest request) {
    User user = userRepository
      .findByIdAndIsDeletedFalse(userId)
      .orElseThrow(() -> ExceptionUtil.notFound(ErrorMessage.USER_NOT_FOUND));

    userMapper.toUser(user, request);

    return userMapper.toUserProfileResponse(userRepository.save(user));
  }

  @Override
  public void updatePassword(Long userId, PasswordUpdateRequest request) {
    User user = userRepository
      .findByIdAndIsDeletedFalse(userId)
      .orElseThrow(() -> ExceptionUtil.notFound(ErrorMessage.USER_NOT_FOUND));

    // Nếu là USER thường thì bắt buộc kiểm tra oldPassword
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    boolean isAdmin = auth.getAuthorities().stream()
      .anyMatch(granted -> granted.getAuthority().equals(RoleName.ADMIN.name()));

    if (!isAdmin) {
      if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
        throw ExceptionUtil.badRequest(ErrorMessage.OLD_PASSWORD_INCORRECT);
      }
    }

    // Cập nhật mật khẩu mới
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);
  }

  @Override
  public void softDeleteUser(Long id) {
    User user = userRepository
      .findByIdAndIsDeletedFalse(id)
      .orElseThrow(() -> ExceptionUtil.notFound(ErrorMessage.USER_NOT_FOUND));

    user.setIsDeleted(true);

    userRepository.save(user);
  }

  @Override
  public PageResponse<UserResponse> searchUsers(
    String fullname, LocalDate dob, boolean includeDeleted, Pageable pageable) {
    Specification<User> spec = includeDeleted
      ? UserSpecification.hasFullNameLike(fullname).and(UserSpecification.hasDobEqual(dob))
      : UserSpecification.isNotDeleted()
      .and(UserSpecification.hasFullNameLike(fullname))
      .and(UserSpecification.hasDobEqual(dob));

    Page<User> userPage = userRepository.findAll(spec, pageable);
    Page<UserResponse> userResponsePage = userPage.map(userMapper::toUserResponse);
    return PageResponse.from(userResponsePage);
  }

  @Override
  public UserProfileResponse getUserById(Long id) {
    User user = userRepository
      .findByIdAndIsDeletedFalse(id)
      .orElseThrow(() -> ExceptionUtil.notFound(ErrorMessage.USER_NOT_FOUND));
    return userMapper.toUserProfileResponse(user);
  }

  @Override
  public UserResponse activeUser(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> ExceptionUtil.notFound(ErrorMessage.USER_NOT_FOUND));

    user.setIsDeleted(false);

    userRepository.save(user);

    return userMapper.toUserResponse(user);
  }

  @Override
  public UserResponse updateRolesForUser(Long userId, UserRoleUpdateRequest request) {
    User user = userRepository.findById(userId).orElseThrow(() -> ExceptionUtil.notFound(ErrorMessage.USER_NOT_FOUND));

    Set<String> requestedRoleNames = request.getRoleNames();
    List<Role> roles = roleRepository.findAllById(requestedRoleNames);

    // Lấy ra danh sách role không tồn tại
    Set<String> foundRoleNames = roles.stream().map(Role::getName).collect(Collectors.toSet());

    Set<String> notFoundRoles = requestedRoleNames.stream()
      .filter(r -> !foundRoleNames.contains(r))
      .collect(Collectors.toSet());

    if (!notFoundRoles.isEmpty()) {
      throw ExceptionUtil.with(
        HttpStatus.NOT_FOUND, ErrorMessage.ROLES_NOT_FOUND, String.join(", ", notFoundRoles));
    }

    user.setRoles(new HashSet<>(roles));

    return userMapper.toUserResponse(userRepository.save(user));
  }

  @Override
  public void resetPassword(String email, String newPassword) {
    User user = userRepository
      .findByEmailAndIsDeletedFalse(email)
      .orElseThrow(() -> ExceptionUtil.notFound(ErrorMessage.USER_NOT_FOUND));
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
  }
}
