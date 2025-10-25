package com.glamora_store.mapper;

import com.glamora_store.dto.request.admin.voucher.VoucherCreateRequest;
import com.glamora_store.dto.request.admin.voucher.VoucherUpdateRequest;
import com.glamora_store.dto.response.common.voucher.VoucherResponse;
import com.glamora_store.entity.Voucher;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface VoucherMapper {

  @Mapping(target = "isDeleted", constant = "false")
  // constant luôn set giá trị cố định, không phụ thuộc vào source
  @Mapping(target = "usedCount", constant = "0")
  @Mapping(target = "isActive", expression = "java(request.getIsActive() != null ? request.getIsActive() : true)")
  @Mapping(target = "usagePerUser", expression = "java(request.getUsagePerUser() != null ? request.getUsagePerUser() : 1)")
  Voucher toVoucher(VoucherCreateRequest request);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void toVoucher(@MappingTarget Voucher voucher, VoucherUpdateRequest request);

  @Mapping(target = "isValid", expression = "java(voucher.isValid())")
  VoucherResponse toVoucherResponse(Voucher voucher);
}
