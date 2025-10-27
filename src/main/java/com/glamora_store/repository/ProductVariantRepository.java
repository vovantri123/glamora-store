package com.glamora_store.repository;

import com.glamora_store.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductVariantRepository
  extends JpaRepository<ProductVariant, Long>, JpaSpecificationExecutor<ProductVariant> {

  Optional<ProductVariant> findByIdAndIsDeletedFalse(Long id);

  boolean existsBySku(String sku);

  /**
   * Get next SKU sequence number using PostgreSQL sequence.
   * Uses database sequence to ensure thread-safety and multi-instance safety.
   * This is a global sequence (not per-product) to avoid SKU conflicts.
   */
  @Query(value = "SELECT nextval('sku_sequence')", nativeQuery = true)
  Long getNextSkuSequence();
}
