package com.glamora_store.mapper;

import com.glamora_store.dto.request.common.address.AddressCreateRequest;
import com.glamora_store.dto.request.common.address.AddressUpdateRequest;
import com.glamora_store.dto.response.common.address.AddressResponse;
import com.glamora_store.entity.Address;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AddressMapper {

  @Mapping(target = "isDefault", source = "default")
  Address toAddress(AddressCreateRequest request);

  @Mapping(target = "isDefault", source = "default")
  void toAddress(@MappingTarget Address address, AddressUpdateRequest request);

  AddressResponse toAddressResponse(Address address);

  List<AddressResponse> toAddressResponseList(List<Address> addresses);
}
