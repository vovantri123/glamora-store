package com.glamora_store.controller.user;

import com.glamora_store.dto.request.user.voucher.ApplyVoucherRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.common.voucher.VoucherResponse;
import com.glamora_store.dto.response.user.voucher.VoucherDiscountResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.VoucherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/user/vouchers")
@RequiredArgsConstructor
public class UserVoucherController {

  private final VoucherService voucherService;

  @GetMapping("/active")
  public ResponseEntity<ApiResponse<List<VoucherResponse>>> getActiveVouchers() {
    List<VoucherResponse> vouchers = voucherService.getActiveVouchers();
    return ResponseEntity.ok(
        ApiResponse.<List<VoucherResponse>>builder()
            .message(SuccessMessage.GET_ALL_VOUCHER_SUCCESS.getMessage())
            .data(vouchers)
            .build()
    );
  }

  @GetMapping("/my-vouchers")
  public ResponseEntity<ApiResponse<List<VoucherResponse>>> getMyVouchers() {
    List<VoucherResponse> vouchers = voucherService.getMyVouchers();
    return ResponseEntity.ok(
        ApiResponse.<List<VoucherResponse>>builder()
            .message(SuccessMessage.GET_ALL_VOUCHER_SUCCESS.getMessage())
            .data(vouchers)
            .build()
    );
  }

  @PostMapping("/collect")
  public ResponseEntity<ApiResponse<VoucherResponse>> collectVoucher(@Valid @RequestBody ApplyVoucherRequest request) {
    VoucherResponse voucher = voucherService.collectVoucher(request.getVoucherCode());
    return ResponseEntity.ok(
        ApiResponse.<VoucherResponse>builder()
            .message(SuccessMessage.COLLECT_VOUCHER_SUCCESS.getMessage())
            .data(voucher)
            .build()
    );
  }

  @PostMapping("/calculate-discount")
  public ResponseEntity<ApiResponse<VoucherDiscountResponse>> calculateVoucherDiscount(
      @Valid @RequestBody ApplyVoucherRequest request,
      @RequestParam BigDecimal orderAmount) {
    VoucherDiscountResponse discount = voucherService.calculateVoucherDiscount(request.getVoucherCode(), orderAmount);
    return ResponseEntity.ok(
        ApiResponse.<VoucherDiscountResponse>builder()
            .message(SuccessMessage.CALCULATE_VOUCHER_DISCOUNT_SUCCESS.getMessage())
            .data(discount)
            .build()
    );
  }
}
