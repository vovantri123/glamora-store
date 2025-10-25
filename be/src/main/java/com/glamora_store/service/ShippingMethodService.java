package com.glamora_store.service;

import com.glamora_store.dto.request.admin.shipping.CreateShippingMethodRequest;
import com.glamora_store.dto.request.admin.shipping.UpdateShippingMethodRequest;
import com.glamora_store.dto.response.common.shipping.ShippingMethodResponse;

import java.util.List;

public interface ShippingMethodService {
  
  // Common endpoints
  List<ShippingMethodResponse> getAllActiveShippingMethods();
  
  ShippingMethodResponse getShippingMethodById(Long id);
  
  // Admin endpoints
  List<ShippingMethodResponse> getAllShippingMethods();
  
  ShippingMethodResponse createShippingMethod(CreateShippingMethodRequest request);
  
  ShippingMethodResponse updateShippingMethod(Long id, UpdateShippingMethodRequest request);
  
  void deleteShippingMethod(Long id);
  
  ShippingMethodResponse toggleActiveStatus(Long id);
}
