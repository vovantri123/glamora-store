package com.glamora_store.mapper;

import com.glamora_store.dto.request.admin.product_variant.ProductVariantCreateRequest;
import com.glamora_store.dto.request.admin.product_variant.ProductVariantUpdateRequest;
import com.glamora_store.dto.response.admin.product_variant.ProductVariantAdminResponse;
import com.glamora_store.entity.ProductVariant;

import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductVariantMapper {

        @Mapping(target = "productId", source = "product.id")
        @Mapping(target = "productName", source = "product.name")
        @Mapping(target = "variantValues", expression = "java(mapVariantValues(productVariant))")
        ProductVariantAdminResponse toProductVariantAdminResponse(ProductVariant productVariant);

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "isDeleted", ignore = true)
        @Mapping(target = "product", ignore = true)
        @Mapping(target = "variantValues", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @Mapping(target = "createdBy", ignore = true)
        @Mapping(target = "updatedBy", ignore = true)
        ProductVariant toProductVariant(ProductVariantCreateRequest request);

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "isDeleted", ignore = true)
        @Mapping(target = "product", ignore = true)
        @Mapping(target = "variantValues", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @Mapping(target = "createdBy", ignore = true)
        @Mapping(target = "updatedBy", ignore = true)
        void updateProductVariantFromRequest(ProductVariantUpdateRequest request,
                        @MappingTarget ProductVariant productVariant);

        default List<ProductVariantAdminResponse.AttributeValueInfo> mapVariantValues(ProductVariant variant) {
                return variant.getVariantValues().stream()
                                .map(pvv -> ProductVariantAdminResponse.AttributeValueInfo.builder()
                                                .attributeValueId(pvv.getAttributeValue().getId())
                                                .attributeId(pvv.getAttributeValue().getAttribute().getId())
                                                .attributeName(pvv.getAttributeValue().getAttribute().getName())
                                                .valueName(pvv.getAttributeValue().getValue())
                                                .build())
                                .collect(Collectors.toList());
        }
}
