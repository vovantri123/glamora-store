package com.glamora_store.service.impl;

import com.glamora_store.dto.request.admin.shipping.CreateShippingMethodRequest;
import com.glamora_store.dto.request.admin.shipping.UpdateShippingMethodRequest;
import com.glamora_store.dto.response.common.shipping.ShippingMethodResponse;
import com.glamora_store.entity.ShippingMethod;
import com.glamora_store.enums.ErrorMessage;
import com.glamora_store.mapper.ShippingMethodMapper;
import com.glamora_store.repository.ShippingMethodRepository;
import com.glamora_store.service.ShippingMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShippingMethodServiceImpl implements ShippingMethodService {

  private final ShippingMethodRepository shippingMethodRepository;
  private final ShippingMethodMapper shippingMethodMapper;

  @Override
  public List<ShippingMethodResponse> getAllActiveShippingMethods() {
    return shippingMethodRepository.findByIsActiveTrue()
      .stream()
      .map(shippingMethodMapper::toShippingMethodResponse)
      .collect(Collectors.toList());
  }

  @Override
  public ShippingMethodResponse getShippingMethodById(Long id) {
    ShippingMethod shippingMethod = findShippingMethodById(id);
    return shippingMethodMapper.toShippingMethodResponse(shippingMethod);
  }

  @Override
  public List<ShippingMethodResponse> getAllShippingMethods() {
    return shippingMethodRepository.findAll()
      .stream()
      .map(shippingMethodMapper::toShippingMethodResponse)
      .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public ShippingMethodResponse createShippingMethod(CreateShippingMethodRequest request) {
    // Check if name already exists
    if (shippingMethodRepository.existsByName(request.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.SHIPPING_METHOD_NAME_EXISTS.getMessage());
    }

    // Check if code already exists
    if (shippingMethodRepository.existsByCode(request.getCode())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.SHIPPING_METHOD_CODE_EXISTS.getMessage());
    }

    ShippingMethod shippingMethod = ShippingMethod.builder()
      .name(request.getName())
      .code(request.getCode())
      .description(request.getDescription())
      .baseFee(request.getBaseFee())
      .feePerKm(request.getFeePerKm())
      .estimatedDays(request.getEstimatedDays())
      .logoUrl(request.getLogoUrl())
      .isActive(request.getIsActive() != null ? request.getIsActive() : true)
      .build();

    ShippingMethod saved = shippingMethodRepository.save(shippingMethod);

    return shippingMethodMapper.toShippingMethodResponse(saved);
  }

  @Override
  @Transactional
  public ShippingMethodResponse updateShippingMethod(Long id, UpdateShippingMethodRequest request) {
    ShippingMethod shippingMethod = findShippingMethodById(id);

    // Check if name already exists for another shipping method
    if (request.getName() != null && !request.getName().equals(shippingMethod.getName())) {
      if (shippingMethodRepository.existsByName(request.getName())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          ErrorMessage.SHIPPING_METHOD_NAME_EXISTS.getMessage());
      }
    }

    // Update entity using MapStruct
    shippingMethodMapper.toShippingMethod(request, shippingMethod);

    ShippingMethod updated = shippingMethodRepository.save(shippingMethod);

    return shippingMethodMapper.toShippingMethodResponse(updated);
  }

  @Override
  @Transactional
  public void deleteShippingMethod(Long id) {
    ShippingMethod shippingMethod = findShippingMethodById(id);
    shippingMethodRepository.delete(shippingMethod);
  }

  @Override
  @Transactional
  public ShippingMethodResponse toggleActiveStatus(Long id) {
    ShippingMethod shippingMethod = findShippingMethodById(id);
    shippingMethod.setIsActive(!shippingMethod.getIsActive());
    ShippingMethod updated = shippingMethodRepository.save(shippingMethod);

    return shippingMethodMapper.toShippingMethodResponse(updated);
  }

  private ShippingMethod findShippingMethodById(Long id) {
    return shippingMethodRepository.findById(id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
        ErrorMessage.SHIPPING_METHOD_NOT_FOUND.getMessage()));
  }
}
