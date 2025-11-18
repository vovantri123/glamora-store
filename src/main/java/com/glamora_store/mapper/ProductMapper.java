package com.glamora_store.mapper;

import com.glamora_store.dto.request.admin.product.ProductCreateRequest;
import com.glamora_store.dto.request.admin.product.ProductUpdateRequest;
import com.glamora_store.dto.response.admin.product.ProductAdminResponse;
import com.glamora_store.dto.response.common.product.ProductImageResponse;
import com.glamora_store.dto.response.common.product.ProductResponse;
import com.glamora_store.dto.response.common.product.ProductVariantResponse;
import com.glamora_store.dto.response.common.product.VariantAttributeResponse;
import com.glamora_store.entity.Product;
import com.glamora_store.entity.ProductImage;
import com.glamora_store.entity.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

  @Mapping(target = "categoryId", source = "category.id")
  @Mapping(target = "categoryName", source = "category.name")
  @Mapping(target = "thumbnailUrl", expression = "java(getThumbnailUrl(product))")
  @Mapping(target = "images", expression = "java(getAllImages(product))")
  @Mapping(target = "minPrice", expression = "java(getMinPrice(product))")
  @Mapping(target = "maxPrice", expression = "java(getMaxPrice(product))")
  @Mapping(target = "totalStock", expression = "java(getTotalStock(product))")
  @Mapping(target = "averageRating", expression = "java(getAverageRating(product))")
  @Mapping(target = "totalReviews", expression = "java(getTotalReviews(product))")
  ProductResponse toProductResponse(Product product);

  List<ProductResponse> toProductResponseList(List<Product> products);

  @Mapping(target = "attributes", expression = "java(mapVariantAttributes(variant))")
  ProductVariantResponse toProductVariantResponse(ProductVariant variant);

  // Admin mappings
  @Mapping(target = "categoryId", source = "category.id")
  @Mapping(target = "categoryName", source = "category.name")
  @Mapping(target = "thumbnailUrl", expression = "java(getThumbnailUrl(product))")
  ProductAdminResponse toProductAdminResponse(Product product);

  List<ProductAdminResponse> toProductAdminResponseList(List<Product> products);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "isDeleted", ignore = true)
  @Mapping(target = "category", ignore = true)
  @Mapping(target = "images", ignore = true)
  @Mapping(target = "variants", ignore = true)
  @Mapping(target = "reviews", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  Product toProduct(ProductCreateRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "isDeleted", ignore = true)
  @Mapping(target = "category", ignore = true)
  @Mapping(target = "images", ignore = true)
  @Mapping(target = "variants", ignore = true)
  @Mapping(target = "reviews", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  void updateProductFromRequest(ProductUpdateRequest request, @MappingTarget Product product);

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

  // Load product images first (sorted by displayOrder), then variant images
  default List<ProductImageResponse> getAllImages(Product product) {
    List<ProductImageResponse> allImages = new ArrayList<>();

    // 1. Add product images sorted by displayOrder
    List<ProductImageResponse> productImages = product.getImages().stream()
        .sorted(Comparator.comparing(ProductImage::getDisplayOrder))
        .map(img -> ProductImageResponse.builder()
            .id(img.getId())
            .imageUrl(img.getImageUrl())
            .altText(img.getAltText())
            .isThumbnail(img.getIsThumbnail())
            .displayOrder(img.getDisplayOrder())
            .build())
        .collect(Collectors.toList());
    allImages.addAll(productImages);

    // 2. Add variant images (each variant has 1 image)
    List<ProductImageResponse> variantImages = product.getVariants().stream()
        .filter(variant -> variant.getImageUrl() != null && !variant.getImageUrl().isEmpty())
        .map(variant -> ProductImageResponse.builder()
            .id(variant.getId()) // Using variant ID for unique identification
            .imageUrl(variant.getImageUrl())
            .altText(variant.getSku())
            .isThumbnail(false)
            .displayOrder(999 + variant.getId().intValue()) // Higher order to place after product images
            .build())
        .collect(Collectors.toList());
    allImages.addAll(variantImages);

    return allImages;
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

  default Double getAverageRating(Product product) {
    return product.getReviews().stream()
        .filter(review -> !review.getIsDeleted())
        .mapToInt(review -> review.getRating())
        .average()
        .orElse(0.0);
  }

  default Long getTotalReviews(Product product) {
    return product.getReviews().stream()
        .filter(review -> !review.getIsDeleted())
        .count();
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
