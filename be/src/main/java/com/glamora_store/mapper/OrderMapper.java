package com.glamora_store.mapper;

import com.glamora_store.dto.response.common.order.OrderItemResponse;
import com.glamora_store.dto.response.common.order.OrderResponse;
import com.glamora_store.entity.Order;
import com.glamora_store.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "userFullName", source = "user.fullName")
    @Mapping(target = "addressId", source = "shippingAddress.id")
    @Mapping(target = "shippingAddressDetail", expression = "java(formatAddress(order))")
    @Mapping(target = "shippingMethodId", source = "shippingMethod.id")
    @Mapping(target = "shippingMethodName", source = "shippingMethod.name")
    @Mapping(target = "voucherId", source = "voucher.id")
    @Mapping(target = "voucherCode", source = "voucher.code")
    @Mapping(target = "orderItems", source = "orderItems")
    OrderResponse toOrderResponse(Order order);

    @Mapping(target = "variantId", source = "variant.id")
    @Mapping(target = "productName", source = "variant.product.name")
    @Mapping(target = "variantName", expression = "java(formatVariantName(orderItem))")
    @Mapping(target = "productImageUrl", expression = "java(getProductImageUrl(orderItem))")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);

    default String formatAddress(Order order) {
        if (order.getShippingAddress() == null) {
            return "";
        }
        var address = order.getShippingAddress();
        return String.format("%s, %s, %s, %s",
                address.getStreetDetail(),
                address.getWard(),
                address.getDistrict(),
                address.getProvince());
    }

    default String formatVariantName(OrderItem orderItem) {
        if (orderItem.getVariant() == null) {
            return "";
        }
        var variant = orderItem.getVariant();
        if (variant.getVariantValues() == null || variant.getVariantValues().isEmpty()) {
            return "Mặc định";
        }
        return variant.getVariantValues().stream()
                .map(vv -> vv.getAttributeValue().getValue())
                .reduce((a, b) -> a + " - " + b)
                .orElse("Mặc định");
    }

    default String getProductImageUrl(OrderItem orderItem) {
        if (orderItem.getVariant() == null || orderItem.getVariant().getProduct() == null) {
            return null;
        }
        var product = orderItem.getVariant().getProduct();
        if (product.getImages() == null || product.getImages().isEmpty()) {
            return null;
        }
        return product.getImages().stream()
                .filter(img -> img.getIsThumbnail() != null && img.getIsThumbnail())
                .findFirst()
                .map(img -> img.getImageUrl())
                .orElse(product.getImages().iterator().next().getImageUrl());
    }
}
