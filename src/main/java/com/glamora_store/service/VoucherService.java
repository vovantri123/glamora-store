package com.glamora_store.service;

import com.glamora_store.dto.request.admin.voucher.VoucherCreateRequest;
import com.glamora_store.dto.request.admin.voucher.VoucherUpdateRequest;
import com.glamora_store.dto.response.common.voucher.VoucherResponse;
import com.glamora_store.dto.response.user.voucher.VoucherDiscountResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface VoucherService {

  // ============================================
  // ADMIN APIs
  // ============================================

  VoucherResponse createVoucher(VoucherCreateRequest request);

  VoucherResponse updateVoucher(Long voucherId, VoucherUpdateRequest request);

  void deleteVoucher(Long voucherId);  // (soft delete)

  Page<VoucherResponse> getAllVouchers(Pageable pageable);

  VoucherResponse getVoucherById(Long voucherId);

  void revokeUserVoucher(Long userId, Long voucherId);

  // ============================================
  // USER APIs
  // ============================================


  // Lấy danh sách voucher có hiệu lực
  List<VoucherResponse> getActiveVouchers();

  // Lấy danh sách voucher của user hiện tại
  List<VoucherResponse> getMyVouchers();

  // Thêm voucher vào tài khoản user bằng code
  VoucherResponse collectVoucher(String voucherCode);

  // Kiểm tra và tính toán giảm giá của voucher
  VoucherDiscountResponse calculateVoucherDiscount(String voucherCode, BigDecimal orderAmount);


}
