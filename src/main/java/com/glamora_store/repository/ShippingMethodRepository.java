package com.glamora_store.repository;

import com.glamora_store.entity.ShippingMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShippingMethodRepository extends JpaRepository<ShippingMethod, Long> {
  List<ShippingMethod> findByIsActiveTrue();

  boolean existsByName(String name);

  boolean existsByCode(String code);
}
