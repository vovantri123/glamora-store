package com.glamora_store.controller.user;

import com.glamora_store.dto.request.common.address.AddressCreateRequest;
import com.glamora_store.dto.request.common.address.AddressUpdateRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.common.address.AddressResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.AddressService;
import com.glamora_store.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/addresses")
public class UserAddressController {
  private final AddressService addressService;

  @PostMapping
  public ApiResponse<AddressResponse> createAddress(@Valid @RequestBody AddressCreateRequest request) {
    Long userId = SecurityUtil.getCurrentUserId();
    return new ApiResponse<>(
      SuccessMessage.CREATE_ADDRESS_SUCCESS.getMessage(),
      addressService.createAddress(userId, request));
  }

  @PutMapping("/{addressId}")
  public ApiResponse<AddressResponse> updateAddress(
    @PathVariable Long addressId,
    @Valid @RequestBody AddressUpdateRequest request) {
    Long userId = SecurityUtil.getCurrentUserId();
    return new ApiResponse<>(
      SuccessMessage.UPDATE_ADDRESS_SUCCESS.getMessage(),
      addressService.updateAddress(userId, addressId, request));
  }

  @DeleteMapping("/{addressId}")
  public ApiResponse<String> deleteAddress(@PathVariable Long addressId) {
    Long userId = SecurityUtil.getCurrentUserId();
    addressService.deleteAddress(userId, addressId);
    return new ApiResponse<>(SuccessMessage.DELETE_ADDRESS_SUCCESS.getMessage());
  }

  @GetMapping
  public ApiResponse<List<AddressResponse>> getAllMyAddresses() {
    Long userId = SecurityUtil.getCurrentUserId();
    List<AddressResponse> addresses = addressService.getAllAddressesByUserId(userId);
    String message = addresses.isEmpty()
      ? SuccessMessage.NO_DATA_FOUND.getMessage()
      : SuccessMessage.GET_ALL_ADDRESS_SUCCESS.getMessage();
    return new ApiResponse<>(message, addresses);
  }

  @GetMapping("/{addressId}")
  public ApiResponse<AddressResponse> getAddressById(@PathVariable Long addressId) {
    Long userId = SecurityUtil.getCurrentUserId();
    return new ApiResponse<>(
      SuccessMessage.GET_ADDRESS_SUCCESS.getMessage(),
      addressService.getAddressById(userId, addressId));
  }

  @GetMapping("/default")
  public ApiResponse<AddressResponse> getDefaultAddress() {
    Long userId = SecurityUtil.getCurrentUserId();
    return new ApiResponse<>(
      SuccessMessage.GET_ADDRESS_SUCCESS.getMessage(),
      addressService.getDefaultAddress(userId));
  }

  @PutMapping("/{addressId}/set-default")
  public ApiResponse<AddressResponse> setDefaultAddress(@PathVariable Long addressId) {
    Long userId = SecurityUtil.getCurrentUserId();
    return new ApiResponse<>(
      SuccessMessage.SET_DEFAULT_ADDRESS_SUCCESS.getMessage(),
      addressService.setIsDefaultAddress(userId, addressId));
  }
}
