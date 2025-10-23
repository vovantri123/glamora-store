package com.glamora_store.mapper;

import com.glamora_store.dto.request.admin.shipping.UpdateShippingMethodRequest;
import com.glamora_store.dto.response.common.shipping.ShippingMethodResponse;
import com.glamora_store.entity.ShippingMethod;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ShippingMethodMapper {
  ShippingMethodResponse toShippingMethodResponse(ShippingMethod shippingMethod);

  void toShippingMethod(UpdateShippingMethodRequest request, @MappingTarget ShippingMethod shippingMethod);
}

