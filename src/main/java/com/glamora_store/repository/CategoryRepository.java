package com.glamora_store.repository;

import com.glamora_store.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  List<Category> findByIsDeletedFalse();

  Optional<Category> findByIdAndIsDeletedFalse(Long id);

  List<Category> findByParentIsNullAndIsDeletedFalse();

  List<Category> findByParentIdAndIsDeletedFalse(Long parentId);

  // Admin queries
  @Query("SELECT c FROM Category c WHERE " +
      "(:keyword IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
      "AND (:includeDeleted = true OR c.isDeleted = false)")
  Page<Category> searchCategories(@Param("keyword") String keyword,
      @Param("includeDeleted") Boolean includeDeleted,
      Pageable pageable);

  Optional<Category> findByName(String name);

  boolean existsByName(String name);
}
