package com.glamora_store.repository;

import com.glamora_store.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductReviewRepository
        extends JpaRepository<ProductReview, Long>, JpaSpecificationExecutor<ProductReview> {

    // Tìm review theo id và chưa bị xóa
    Optional<ProductReview> findByIdAndIsDeletedFalse(Long id);

    // Kiểm tra user đã review sản phẩm này chưa
    boolean existsByUserIdAndProductIdAndIsDeletedFalse(Long userId, Long productId);

    // Tính rating trung bình của một sản phẩm (cần custom query cho AVG)
    @org.springframework.data.jpa.repository.Query("SELECT AVG(pr.rating) FROM ProductReview pr WHERE pr.product.id = :productId AND pr.isDeleted = false")
    Double getAverageRatingByProductId(@org.springframework.data.repository.query.Param("productId") Long productId);

    // Đếm số lượng review của một sản phẩm
    Long countByProductIdAndIsDeletedFalse(Long productId);
}
