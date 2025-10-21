package com.glamora_store.service.impl;

import com.glamora_store.dto.request.common.address.AddressCreateRequest;
import com.glamora_store.dto.request.common.address.AddressUpdateRequest;
import com.glamora_store.dto.response.common.address.AddressResponse;
import com.glamora_store.entity.Address;
import com.glamora_store.entity.User;
import com.glamora_store.enums.ErrorMessage;
import com.glamora_store.mapper.AddressMapper;
import com.glamora_store.repository.AddressRepository;
import com.glamora_store.repository.UserRepository;
import com.glamora_store.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {

  private final AddressRepository addressRepository;
  private final UserRepository userRepository;
  private final AddressMapper addressMapper;

  /**
   * Đảm bảo chỉ có 1 địa chỉ mặc định cho mỗi user
   * Set tất cả địa chỉ khác của user thành không mặc định
   */
  private void ensureSingleDefaultAddress(Long userId, Long excludeAddressId) {
    List<Address> existingAddresses = addressRepository.findByUser_IdAndIsDeletedFalse(userId);
    existingAddresses.stream()
        .filter(addr -> !addr.getId().equals(excludeAddressId))
        .forEach(addr -> addr.setIsDefault(false));
    addressRepository.saveAll(existingAddresses);
  }

  @Override
  @Transactional
  public AddressResponse createAddress(Long userId, AddressCreateRequest request) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.USER_NOT_FOUND.getMessage()));

    if (Boolean.TRUE.equals(user.getIsDeleted())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.USER_DELETED.getMessage());
    }

    Address address = addressMapper.toAddress(request);
    address.setUser(user);

    // Nếu đây là địa chỉ đầu tiên hoặc được set là default, cập nhật các địa chỉ
    // khác
    if (request.isDefault() || addressRepository.countByUser_IdAndIsDeletedFalse(userId) == 0) {
      ensureSingleDefaultAddress(userId, null);
      address.setIsDefault(true);
    }

    Address savedAddress = addressRepository.save(address);
    return addressMapper.toAddressResponse(savedAddress);
  }

  @Override
  @Transactional
  public AddressResponse updateAddress(Long userId, Long addressId, AddressUpdateRequest request) {
    Address address = addressRepository.findByIdAndUserId(addressId, userId)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ADDRESS_NOT_FOUND.getMessage()));

    addressMapper.toAddress(address, request);

    // Nếu set làm địa chỉ mặc định, cập nhật các địa chỉ khác
    if (request.isDefault()) {
      ensureSingleDefaultAddress(userId, addressId);
    }

    Address updatedAddress = addressRepository.save(address);
    return addressMapper.toAddressResponse(updatedAddress);
  }

  @Override
  @Transactional
  public void deleteAddress(Long userId, Long addressId) {
    Address address = addressRepository.findByIdAndUserId(addressId, userId)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ADDRESS_NOT_FOUND.getMessage()));

    boolean wasDefault = address.getIsDefault();
    address.setIsDeleted(true);
    address.setIsDefault(false);
    addressRepository.save(address);

    // Nếu xóa địa chỉ mặc định, set địa chỉ đầu tiên còn lại làm mặc định
    if (wasDefault) {
      List<Address> remainingAddresses = addressRepository.findByUser_IdAndIsDeletedFalse(userId);
      if (!remainingAddresses.isEmpty()) {
        Address firstAddress = remainingAddresses.get(0);
        firstAddress.setIsDefault(true);
        addressRepository.save(firstAddress);
      }
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<AddressResponse> getAllAddressesByUserId(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.USER_NOT_FOUND.getMessage()));

    if (Boolean.TRUE.equals(user.getIsDeleted())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.USER_DELETED.getMessage());
    }

    List<Address> addresses = addressRepository.findByUser_IdAndIsDeletedFalse(userId);
    return addressMapper.toAddressResponseList(addresses);
  }

  @Override
  @Transactional(readOnly = true)
  public AddressResponse getAddressById(Long userId, Long addressId) {
    Address address = addressRepository.findByIdAndUserId(addressId, userId)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ADDRESS_NOT_FOUND.getMessage()));

    return addressMapper.toAddressResponse(address);
  }

  @Override
  @Transactional(readOnly = true)
  public AddressResponse getDefaultAddress(Long userId) {
    Address address = addressRepository.findByUser_IdAndIsDefaultTrueAndIsDeletedFalse(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            ErrorMessage.DEFAULT_ADDRESS_NOT_FOUND.getMessage()));

    return addressMapper.toAddressResponse(address);
  }

  @Override
  @Transactional
  public AddressResponse setIsDefaultAddress(Long userId, Long addressId) {
    Address address = addressRepository.findByIdAndUserId(addressId, userId)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ADDRESS_NOT_FOUND.getMessage()));

    // Set tất cả địa chỉ khác thành không default
    ensureSingleDefaultAddress(userId, addressId);

    // Set địa chỉ này làm default
    address.setIsDefault(true);
    Address updatedAddress = addressRepository.save(address);

    return addressMapper.toAddressResponse(updatedAddress);
  }
}
