package com.glamora_store.repository;

import com.glamora_store.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

  List<Address> findByUser_IdAndIsDeletedFalse(Long userId);

  @Query("SELECT a FROM Address a WHERE a.id = :addressId AND a.user.id = :userId AND a.isDeleted = false")
  Optional<Address> findByIdAndUserId(@Param("addressId") Long addressId, @Param("userId") Long userId);

  Optional<Address> findByUser_IdAndIsDefaultTrueAndIsDeletedFalse(Long userId);

  long countByUser_IdAndIsDeletedFalse(Long userId);
}
