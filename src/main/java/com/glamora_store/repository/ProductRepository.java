package com.glamora_store.repository;

import com.glamora_store.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  Page<Product> findByIsDeletedFalse(Pageable pageable);

  Optional<Product> findByIdAndIsDeletedFalse(Long id);

  Page<Product> findByCategoryIdAndIsDeletedFalse(Long categoryId, Pageable pageable);

  @Query(value = "SELECT * FROM products p WHERE p.is_deleted = false " +
      "AND (:categoryId IS NULL OR p.category_id = :categoryId) " +
      "AND (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))", nativeQuery = true)
  Page<Product> searchProducts(@Param("categoryId") Long categoryId,
      @Param("keyword") String keyword,
      Pageable pageable);

  // Admin queries
  @Query("SELECT p FROM Product p WHERE " +
      "(:categoryId IS NULL OR p.category.id = :categoryId) " +
      "AND (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
      "AND (:includeDeleted = true OR p.isDeleted = false)")
  Page<Product> searchProductsForAdmin(@Param("categoryId") Long categoryId,
      @Param("keyword") String keyword,
      @Param("includeDeleted") Boolean includeDeleted,
      Pageable pageable);

  boolean existsByName(String name);
}
