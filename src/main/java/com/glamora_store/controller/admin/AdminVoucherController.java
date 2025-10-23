package com.glamora_store.controller.admin;

import com.glamora_store.dto.request.admin.voucher.VoucherCreateRequest;
import com.glamora_store.dto.request.admin.voucher.VoucherUpdateRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.common.PageResponse;
import com.glamora_store.dto.response.common.voucher.VoucherResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.VoucherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/vouchers")
@RequiredArgsConstructor
public class AdminVoucherController {

  private final VoucherService voucherService;

  @PostMapping
  public ResponseEntity<ApiResponse<VoucherResponse>> createVoucher(@Valid @RequestBody VoucherCreateRequest request) {
    VoucherResponse voucher = voucherService.createVoucher(request);
    return ResponseEntity.ok(
        ApiResponse.<VoucherResponse>builder()
            .message(SuccessMessage.CREATE_VOUCHER_SUCCESS.getMessage())
            .data(voucher)
            .build()
    );
  }

  @PutMapping("/{voucherId}")
  public ResponseEntity<ApiResponse<VoucherResponse>> updateVoucher(
      @PathVariable Long voucherId,
      @Valid @RequestBody VoucherUpdateRequest request) {
    VoucherResponse voucher = voucherService.updateVoucher(voucherId, request);
    return ResponseEntity.ok(
        ApiResponse.<VoucherResponse>builder()
            .message(SuccessMessage.UPDATE_VOUCHER_SUCCESS.getMessage())
            .data(voucher)
            .build()
    );
  }

  @DeleteMapping("/{voucherId}")
  public ResponseEntity<ApiResponse<Void>> deleteVoucher(@PathVariable Long voucherId) {
    voucherService.deleteVoucher(voucherId);
    return ResponseEntity.ok(
        ApiResponse.<Void>builder()
            .message(SuccessMessage.DELETE_VOUCHER_SUCCESS.getMessage())
            .build()
    );
  }

  @GetMapping
  public ResponseEntity<ApiResponse<PageResponse<VoucherResponse>>> getAllVouchers(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "DESC") String sortDirection) {

    Sort sort = sortDirection.equalsIgnoreCase("ASC")
        ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();

    Pageable pageable = PageRequest.of(page, size, sort);
    Page<VoucherResponse> voucherPage = voucherService.getAllVouchers(pageable);

    PageResponse<VoucherResponse> pageResponse = PageResponse.from(voucherPage);

    return ResponseEntity.ok(
        ApiResponse.<PageResponse<VoucherResponse>>builder()
            .message(SuccessMessage.GET_ALL_VOUCHER_SUCCESS.getMessage())
            .data(pageResponse)
            .build()
    );
  }

  @GetMapping("/{voucherId}")
  public ResponseEntity<ApiResponse<VoucherResponse>> getVoucherById(@PathVariable Long voucherId) {
    VoucherResponse voucher = voucherService.getVoucherById(voucherId);
    return ResponseEntity.ok(
        ApiResponse.<VoucherResponse>builder()
            .message(SuccessMessage.GET_VOUCHER_SUCCESS.getMessage())
            .data(voucher)
            .build()
    );
  }

  @DeleteMapping("/users/{userId}/vouchers/{voucherId}")
  public ResponseEntity<ApiResponse<Void>> revokeUserVoucher(
      @PathVariable Long userId,
      @PathVariable Long voucherId) {
    voucherService.revokeUserVoucher(userId, voucherId);
    return ResponseEntity.ok(
        ApiResponse.<Void>builder()
            .message(SuccessMessage.REVOKE_VOUCHER_SUCCESS.getMessage())
            .build()
    );
  }
}
