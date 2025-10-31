package com.glamora_store.mapper;

import com.glamora_store.dto.request.admin.product_image.ProductImageCreateRequest;
import com.glamora_store.dto.request.admin.product_image.ProductImageUpdateRequest;
import com.glamora_store.dto.response.admin.product_image.ProductImageAdminResponse;
import com.glamora_store.entity.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductImageMapper {

  @Mapping(target = "productId", source = "product.id")
  @Mapping(target = "productName", source = "product.name")
  ProductImageAdminResponse toProductImageAdminResponse(ProductImage productImage);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "product", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  ProductImage toProductImage(ProductImageCreateRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "product", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  void updateProductImageFromRequest(ProductImageUpdateRequest request, @MappingTarget ProductImage productImage);
}
