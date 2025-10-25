package com.glamora_store.controller.admin;

import com.glamora_store.dto.request.common.address.AddressCreateRequest;
import com.glamora_store.dto.request.common.address.AddressUpdateRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.common.address.AddressResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/addresses")
public class AdminAddressController {
  private final AddressService addressService;

  @PostMapping("/users/{userId}")
  public ApiResponse<AddressResponse> createAddressForUser(
    @PathVariable Long userId,
    @Valid @RequestBody AddressCreateRequest request) {
    return new ApiResponse<>(
      SuccessMessage.CREATE_ADDRESS_SUCCESS.getMessage(),
      addressService.createAddress(userId, request));
  }

  @PutMapping("/users/{userId}/addresses/{addressId}")
  public ApiResponse<AddressResponse> updateAddressForUser(
    @PathVariable Long userId,
    @PathVariable Long addressId,
    @Valid @RequestBody AddressUpdateRequest request) {
    return new ApiResponse<>(
      SuccessMessage.UPDATE_ADDRESS_SUCCESS.getMessage(),
      addressService.updateAddress(userId, addressId, request));
  }

  @DeleteMapping("/users/{userId}/addresses/{addressId}")
  public ApiResponse<String> deleteAddressForUser(
    @PathVariable Long userId,
    @PathVariable Long addressId) {
    addressService.deleteAddress(userId, addressId);
    return new ApiResponse<>(SuccessMessage.DELETE_ADDRESS_SUCCESS.getMessage());
  }

  @GetMapping("/users/{userId}")
  public ApiResponse<List<AddressResponse>> getAllAddressesByUser(@PathVariable Long userId) {
    List<AddressResponse> addresses = addressService.getAllAddressesByUserId(userId);
    String message = addresses.isEmpty()
      ? SuccessMessage.NO_DATA_FOUND.getMessage()
      : SuccessMessage.GET_ALL_ADDRESS_SUCCESS.getMessage();
    return new ApiResponse<>(message, addresses);
  }

  @GetMapping("/users/{userId}/addresses/{addressId}")
  public ApiResponse<AddressResponse> getAddressById(
    @PathVariable Long userId,
    @PathVariable Long addressId) {
    return new ApiResponse<>(
      SuccessMessage.GET_ADDRESS_SUCCESS.getMessage(),
      addressService.getAddressById(userId, addressId));
  }

  @GetMapping("/users/{userId}/default")
  public ApiResponse<AddressResponse> getDefaultAddressForUser(@PathVariable Long userId) {
    return new ApiResponse<>(
      SuccessMessage.GET_ADDRESS_SUCCESS.getMessage(),
      addressService.getDefaultAddress(userId));
  }

  @PutMapping("/users/{userId}/addresses/{addressId}/set-default")
  public ApiResponse<AddressResponse> setDefaultAddressForUser(
    @PathVariable Long userId,
    @PathVariable Long addressId) {
    return new ApiResponse<>(
      SuccessMessage.SET_DEFAULT_ADDRESS_SUCCESS.getMessage(),
      addressService.setIsDefaultAddress(userId, addressId));
  }
}
