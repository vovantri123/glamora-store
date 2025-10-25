package com.glamora_store.mapper;

import com.glamora_store.dto.response.common.product.ProductResponse;
import com.glamora_store.dto.response.common.product.ProductVariantResponse;
import com.glamora_store.dto.response.common.product.VariantAttributeResponse;
import com.glamora_store.entity.Product;
import com.glamora_store.entity.ProductImage;
import com.glamora_store.entity.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

  @Mapping(target = "categoryId", source = "category.id")
  @Mapping(target = "categoryName", source = "category.name")
  @Mapping(target = "thumbnailUrl", expression = "java(getThumbnailUrl(product))")
  @Mapping(target = "minPrice", expression = "java(getMinPrice(product))")
  @Mapping(target = "maxPrice", expression = "java(getMaxPrice(product))")
  @Mapping(target = "totalStock", expression = "java(getTotalStock(product))")
  ProductResponse toProductResponse(Product product);

  List<ProductResponse> toProductResponseList(List<Product> products);

  @Mapping(target = "attributes", expression = "java(mapVariantAttributes(variant))")
  ProductVariantResponse toProductVariantResponse(ProductVariant variant);

  default String getThumbnailUrl(Product product) {
    return product.getImages().stream() // 1. Lấy stream từ danh sách images của product
      .filter(ProductImage::getIsThumbnail) // 2. Lọc chỉ giữ lại những image có isThumbnail = true
      .findFirst() // 3. Lấy phần tử đầu tiên (nếu có) từ stream đã lọc
      .map(ProductImage::getImageUrl) // 4. Chuyển đổi (map) từ ProductImage sang imageUrl (String)
      .or(() -> product.getImages().stream() // 5. Nếu không có thumbnail, fallback: lấy stream lại từ images
        .findFirst() // 6. Lấy hình ảnh đầu tiên bất kỳ
        .map(ProductImage::getImageUrl)) // 7. Chuyển đổi sang imageUrl
      .orElse(null); // 8. Nếu vẫn không có, trả về null
  }

  default BigDecimal getMinPrice(Product product) {
    return product.getVariants().stream() // 1. Lấy stream từ danh sách variants của product
      .map(ProductVariant::getPrice) // 2. Chuyển đổi (map) mỗi variant thành giá (BigDecimal)
      .min(BigDecimal::compareTo) // 3. Tìm giá trị nhỏ nhất từ stream
      .orElse(BigDecimal.ZERO); // 4. Nếu stream rỗng (không có variant), trả về 0
  }

  default BigDecimal getMaxPrice(Product product) {
    return product.getVariants().stream()
      .map(ProductVariant::getPrice)
      .max(BigDecimal::compareTo)
      .orElse(BigDecimal.ZERO);
  }

  default Integer getTotalStock(Product product) {
    return product.getVariants().stream()
      .mapToInt(ProductVariant::getStock)
      .sum();
  }

  default List<VariantAttributeResponse> mapVariantAttributes(ProductVariant variant) {
    return variant.getVariantValues().stream()
      .map(pvv -> VariantAttributeResponse.builder() // Duyệt qua từng ProductVariantValue
        .attributeName(pvv.getAttributeValue().getAttribute().getName())
        .attributeValue(pvv.getAttributeValue().getValue())
        .build())
      .collect(Collectors.toList());
  }
}
