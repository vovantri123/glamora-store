package com.glamora_store.controller.user;

import com.glamora_store.dto.request.user.voucher.ApplyVoucherRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.common.voucher.VoucherResponse;
import com.glamora_store.dto.response.user.voucher.VoucherDiscountResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.VoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/user/vouchers")
@RequiredArgsConstructor
@Tag(name = "User Voucher Management", description = "APIs for users to manage vouchers - collect, apply, and calculate discounts")
public class UserVoucherController {

  private final VoucherService voucherService;

  @GetMapping("/active")
  @Operation(summary = "Get active vouchers", description = "Retrieve all currently active vouchers that users can collect or apply")
  public ResponseEntity<ApiResponse<List<VoucherResponse>>> getActiveVouchers() {
    List<VoucherResponse> vouchers = voucherService.getActiveVouchers();
    return ResponseEntity.ok(
        ApiResponse.<List<VoucherResponse>>builder()
            .message(SuccessMessage.GET_ALL_VOUCHER_SUCCESS.getMessage())
            .data(vouchers)
            .build());
  }

  @GetMapping("/my-vouchers")
  @Operation(summary = "Get my collected vouchers", description = "Retrieve vouchers that the current user has collected")
  public ResponseEntity<ApiResponse<List<VoucherResponse>>> getMyVouchers() {
    List<VoucherResponse> vouchers = voucherService.getMyVouchers();
    return ResponseEntity.ok(
        ApiResponse.<List<VoucherResponse>>builder()
            .message(SuccessMessage.GET_ALL_VOUCHER_SUCCESS.getMessage())
            .data(vouchers)
            .build());
  }

  @PostMapping("/collect")
  @Operation(summary = "Collect voucher (Traditional Flow)", description = "Collect a voucher to user's account. User must collect voucher first before using it in calculate-discount endpoint. This is the traditional 2-step flow: 1) Collect → 2) Use")
  public ResponseEntity<ApiResponse<VoucherResponse>> collectVoucher(@Valid @RequestBody ApplyVoucherRequest request) {
    VoucherResponse voucher = voucherService.collectVoucher(request.getVoucherCode());
    return ResponseEntity.ok(
        ApiResponse.<VoucherResponse>builder()
            .message(SuccessMessage.COLLECT_VOUCHER_SUCCESS.getMessage())
            .data(voucher)
            .build());
  }

  @PostMapping("/calculate-discount")
  @Operation(summary = "Calculate discount (Requires Collected Voucher)", description = "Calculate voucher discount for checkout. User MUST have collected the voucher first using /collect endpoint. This validates that user owns the voucher and hasn't exceeded usage limits")
  public ResponseEntity<ApiResponse<VoucherDiscountResponse>> calculateVoucherDiscount(
      @Valid @RequestBody ApplyVoucherRequest request,
      @RequestParam BigDecimal orderAmount) {
    VoucherDiscountResponse discount = voucherService.calculateVoucherDiscount(request.getVoucherCode(), orderAmount);
    return ResponseEntity.ok(
        ApiResponse.<VoucherDiscountResponse>builder()
            .message(SuccessMessage.CALCULATE_VOUCHER_DISCOUNT_SUCCESS.getMessage())
            .data(discount)
            .build());
  }

  @PostMapping("/apply")
  @Operation(summary = "Apply voucher directly (Direct Flow)", description = "Apply voucher directly without collecting first. This is the modern 1-step flow for checkout: User enters voucher code → System validates and applies discount immediately. No need to collect voucher beforehand")
  // usedCount chỉ tăng khi Payment SUCCESS (COD hoặc VNPay "00")
  public ResponseEntity<ApiResponse<VoucherDiscountResponse>> applyVoucherDirectly(
      @Valid @RequestBody ApplyVoucherRequest request,
      @RequestParam BigDecimal orderAmount) {
    VoucherDiscountResponse discount = voucherService.applyVoucherDirectly(request.getVoucherCode(), orderAmount);
    return ResponseEntity.ok(
        ApiResponse.<VoucherDiscountResponse>builder()
            .message(SuccessMessage.APPLY_VOUCHER_SUCCESS.getMessage())
            .data(discount)
            .build());
  }
}
