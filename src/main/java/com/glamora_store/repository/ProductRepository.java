package com.glamora_store.repository;

import com.glamora_store.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

        Page<Product> findByIsDeletedFalse(Pageable pageable);

        Optional<Product> findByIdAndIsDeletedFalse(Long id);

        Page<Product> findByCategoryIdAndIsDeletedFalse(Long categoryId, Pageable pageable);

        @Query(value = "SELECT DISTINCT p.* FROM products p " +
                        "LEFT JOIN product_variants pv ON p.id = pv.product_id AND pv.is_deleted = false " +
                        "LEFT JOIN categories c ON p.category_id = c.id " +
                        "WHERE p.is_deleted = false " +
                        "AND (:categoryId IS NULL OR " +
                        "    p.category_id = :categoryId OR " +
                        "    c.parent_id = :categoryId OR " +
                        "    c.id IN (SELECT id FROM categories WHERE parent_id IN (SELECT id FROM categories WHERE parent_id = :categoryId))) "
                        +
                        "AND (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
                        "AND (:minPrice IS NULL OR pv.price >= :minPrice) " +
                        "AND (:maxPrice IS NULL OR pv.price <= :maxPrice)", nativeQuery = true)
        Page<Product> searchProducts(@Param("categoryId") Long categoryId,
                        @Param("keyword") String keyword,
                        @Param("minPrice") BigDecimal minPrice,
                        @Param("maxPrice") BigDecimal maxPrice,
                        Pageable pageable);

        boolean existsByName(String name);
}
