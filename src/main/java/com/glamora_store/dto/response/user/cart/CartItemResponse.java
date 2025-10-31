package com.glamora_store.dto.response.user.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {

  private Long id;
  private CartProductVariantResponse variant;
  private Integer quantity;
  private BigDecimal subtotal; // quantity * price

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class CartProductVariantResponse {
    private Long id;
    private String sku;
    private BigDecimal price;
    private BigDecimal compareAtPrice;
    private Integer stock;
    private List<VariantAttributeResponse> attributes;
    private ProductInfo product;
    private String imageUrl;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class ProductInfo {
    private Long id;
    private String name;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class VariantAttributeResponse {
    private Long attributeId;
    private String attributeName;
    private Long valueId;
    private String valueName;
  }
}
