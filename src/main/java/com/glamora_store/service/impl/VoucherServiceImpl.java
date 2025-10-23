package com.glamora_store.service.impl;

import com.glamora_store.dto.request.admin.voucher.VoucherCreateRequest;
import com.glamora_store.dto.request.admin.voucher.VoucherUpdateRequest;
import com.glamora_store.dto.response.common.voucher.VoucherResponse;
import com.glamora_store.dto.response.user.voucher.VoucherDiscountResponse;
import com.glamora_store.entity.User;
import com.glamora_store.entity.UserVoucher;
import com.glamora_store.entity.Voucher;
import com.glamora_store.enums.ErrorMessage;
import com.glamora_store.mapper.VoucherMapper;
import com.glamora_store.repository.UserRepository;
import com.glamora_store.repository.UserVoucherRepository;
import com.glamora_store.repository.VoucherRepository;
import com.glamora_store.service.VoucherService;
import com.glamora_store.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

  private final VoucherRepository voucherRepository;
  private final UserVoucherRepository userVoucherRepository;
  private final UserRepository userRepository;
  private final VoucherMapper voucherMapper;

  @Override
  @Transactional
  public VoucherResponse createVoucher(VoucherCreateRequest request) {
    // Kiểm tra mã voucher đã tồn tại chưa
    if (voucherRepository.existsByCodeAndIsDeletedFalse(request.getCode())) {
      throw new IllegalArgumentException(ErrorMessage.VOUCHER_CODE_ALREADY_EXISTS.getMessage());
    }

    // Validate ngày
    if (request.getEndDate().isBefore(request.getStartDate())) {
      throw new IllegalArgumentException(ErrorMessage.END_DATE_INVALID.getMessage());
    }

    Voucher voucher = voucherMapper.toEntity(request);
    Voucher savedVoucher = voucherRepository.save(voucher);

    return voucherMapper.toResponse(savedVoucher);
  }

  @Override
  @Transactional
  public VoucherResponse updateVoucher(Long voucherId, VoucherUpdateRequest request) {
    Voucher voucher = voucherRepository.findByIdAndIsDeletedFalse(voucherId)
        .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.VOUCHER_NOT_FOUND.getMessage()));

    // Validate ngày nếu có cập nhật
    LocalDateTime startDate = request.getStartDate() != null ? request.getStartDate() : voucher.getStartDate();
    LocalDateTime endDate = request.getEndDate() != null ? request.getEndDate() : voucher.getEndDate();
    if (endDate.isBefore(startDate)) {
      throw new IllegalArgumentException(ErrorMessage.END_DATE_INVALID.getMessage());
    }

    voucherMapper.updateEntity(voucher, request);
    Voucher savedVoucher = voucherRepository.save(voucher);

    return voucherMapper.toResponse(savedVoucher);
  }

  @Override
  @Transactional
  public void deleteVoucher(Long voucherId) {
    Voucher voucher = voucherRepository.findByIdAndIsDeletedFalse(voucherId)
        .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.VOUCHER_NOT_FOUND.getMessage()));

    voucher.setIsDeleted(true);
    voucherRepository.save(voucher);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<VoucherResponse> getAllVouchers(Pageable pageable) {
    Page<Voucher> vouchers = voucherRepository.findAllByIsDeletedFalse(pageable);
    return vouchers.map(voucherMapper::toResponse);
  }

  @Override
  @Transactional(readOnly = true)
  public VoucherResponse getVoucherById(Long voucherId) {
    Voucher voucher = voucherRepository.findByIdAndIsDeletedFalse(voucherId)
        .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.VOUCHER_NOT_FOUND.getMessage()));

    return voucherMapper.toResponse(voucher);
  }

  @Override
  @Transactional(readOnly = true)
  public List<VoucherResponse> getActiveVouchers() {
    LocalDateTime now = LocalDateTime.now();
    List<Voucher> vouchers = voucherRepository.findActiveVouchers(now);

    return vouchers.stream()
        .map(voucherMapper::toResponse)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<VoucherResponse> getMyVouchers() {
    Long userId = SecurityUtil.getCurrentUserId();
    List<UserVoucher> userVouchers = userVoucherRepository.findAllByUserIdAndIsDeletedFalse(userId);

    return userVouchers.stream()
        .map(UserVoucher::getVoucher)
        .filter(v -> !Boolean.TRUE.equals(v.getIsDeleted()))
        .map(voucherMapper::toResponse)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public VoucherResponse collectVoucher(String voucherCode) {
    Long userId = SecurityUtil.getCurrentUserId();

    // Tìm voucher
    Voucher voucher = voucherRepository.findByCodeAndIsDeletedFalse(voucherCode)
        .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.VOUCHER_NOT_FOUND.getMessage()));

    // Kiểm tra voucher có hiệu lực không
    validateVoucherAvailability(voucher);

    // Kiểm tra user đã thu thập voucher này chưa
    if (userVoucherRepository.findByUserIdAndVoucherIdAndIsDeletedFalse(userId, voucher.getId()).isPresent()) {
      throw new IllegalArgumentException(ErrorMessage.VOUCHER_ALREADY_COLLECTED.getMessage());
    }

    // Tìm user
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.USER_NOT_FOUND.getMessage()));

    // Tạo UserVoucher
    UserVoucher userVoucher = UserVoucher.builder()
        .user(user)
        .voucher(voucher)
        .isDeleted(false)
        .build();

    userVoucherRepository.save(userVoucher);

    return voucherMapper.toResponse(voucher);
  }

  @Override
  @Transactional(readOnly = true)
  public VoucherDiscountResponse calculateVoucherDiscount(String voucherCode, BigDecimal orderAmount) {
    Long userId = SecurityUtil.getCurrentUserId();

    // Tìm voucher
    Voucher voucher = voucherRepository.findByCodeAndIsDeletedFalse(voucherCode)
        .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.VOUCHER_NOT_FOUND.getMessage()));

    // Kiểm tra voucher có hiệu lực không
    validateVoucherAvailability(voucher);

    // Kiểm tra user có voucher này không
    userVoucherRepository.findByUserIdAndVoucherIdAndIsDeletedFalse(userId, voucher.getId())
        .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.USER_VOUCHER_NOT_FOUND.getMessage()));

    // Kiểm tra user đã dùng voucher này bao nhiêu lần
    int usageCount = userVoucherRepository.countByUserIdAndVoucherIdAndIsDeletedFalse(userId, voucher.getId());
    if (usageCount >= voucher.getUsagePerUser()) {
      throw new IllegalArgumentException(ErrorMessage.VOUCHER_USER_LIMIT_EXCEEDED.getMessage());
    }

    // Kiểm tra giá trị đơn hàng tối thiểu
    if (voucher.getMinOrderValue() != null && orderAmount.compareTo(voucher.getMinOrderValue()) < 0) {
      throw new IllegalArgumentException(ErrorMessage.VOUCHER_MIN_ORDER_NOT_MET.getMessage());
    }

    // Tính toán giảm giá
    BigDecimal discountAmount = voucher.calculateDiscount(orderAmount);
    BigDecimal finalAmount = orderAmount.subtract(discountAmount);

    return VoucherDiscountResponse.builder()
        .voucherCode(voucherCode)
        .discountAmount(discountAmount)
        .originalAmount(orderAmount)
        .finalAmount(finalAmount)
        .build();
  }

  @Override
  @Transactional
  public void revokeUserVoucher(Long userId, Long voucherId) {
    UserVoucher userVoucher = userVoucherRepository.findByUserIdAndVoucherIdAndIsDeletedFalse(userId, voucherId)
        .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.USER_VOUCHER_NOT_FOUND.getMessage()));

    userVoucher.setIsDeleted(true);
    userVoucherRepository.save(userVoucher);

  }

  /**
   * Validate voucher có khả dụng không
   */
  private void validateVoucherAvailability(Voucher voucher) {
    LocalDateTime now = LocalDateTime.now();

    // Kiểm tra voucher có active không
    if (!Boolean.TRUE.equals(voucher.getIsActive())) {
      throw new IllegalArgumentException(ErrorMessage.VOUCHER_INACTIVE.getMessage());
    }

    // Kiểm tra thời gian
    if (now.isBefore(voucher.getStartDate())) {
      throw new IllegalArgumentException(ErrorMessage.VOUCHER_NOT_STARTED.getMessage());
    }
    if (now.isAfter(voucher.getEndDate())) {
      throw new IllegalArgumentException(ErrorMessage.VOUCHER_EXPIRED.getMessage());
    }

    // Kiểm tra usage limit
    if (voucher.getUsageLimit() != null && voucher.getUsedCount() >= voucher.getUsageLimit()) {
      throw new IllegalArgumentException(ErrorMessage.VOUCHER_USAGE_LIMIT_EXCEEDED.getMessage());
    }
  }
}
