package com.glamora_store.mapper;

import com.glamora_store.dto.request.common.address.AddressCreateRequest;
import com.glamora_store.dto.request.common.address.AddressUpdateRequest;
import com.glamora_store.dto.response.common.address.AddressResponse;
import com.glamora_store.entity.Address;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "isDeleted", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  Address toAddress(AddressCreateRequest request);

  @Mapping(target = "updatedAt", source = "updatedAt")
  AddressResponse toAddressResponse(Address address);

  List<AddressResponse> toAddressResponseList(List<Address> addresses);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "isDeleted", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateAddress(@MappingTarget Address address, AddressUpdateRequest request);
}
