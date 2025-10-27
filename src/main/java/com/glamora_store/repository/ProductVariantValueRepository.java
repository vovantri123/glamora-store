package com.glamora_store.repository;

import com.glamora_store.entity.ProductVariantValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariantValueRepository extends JpaRepository<ProductVariantValue, Long> {

  void deleteByVariantId(Long variantId);
}
