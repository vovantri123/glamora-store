package com.glamora_store.service;

import com.glamora_store.dto.request.common.address.AddressCreateRequest;
import com.glamora_store.dto.request.common.address.AddressUpdateRequest;
import com.glamora_store.dto.response.common.address.AddressResponse;

import java.util.List;

public interface AddressService {
  AddressResponse createAddress(Long userId, AddressCreateRequest request);

  AddressResponse updateAddress(Long userId, Long addressId, AddressUpdateRequest request);

  void deleteAddress(Long userId, Long addressId);

  List<AddressResponse> getAllAddressesByUserId(Long userId);

  AddressResponse getAddressById(Long userId, Long addressId);

  AddressResponse getDefaultAddress(Long userId);

  AddressResponse setIsDefaultAddress(Long userId, Long addressId);
}
