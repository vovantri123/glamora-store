package com.glamora_store.repository;

import com.glamora_store.entity.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
  Page<ProductImage> findByProductId(Long productId, Pageable pageable);

  Page<ProductImage> findByVariantId(Long variantId, Pageable pageable);

  Page<ProductImage> findByProductIdAndVariantId(Long productId, Long variantId, Pageable pageable);


}
