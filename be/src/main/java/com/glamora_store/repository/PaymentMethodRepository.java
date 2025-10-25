package com.glamora_store.repository;

import com.glamora_store.entity.PaymentMethod;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

  Optional<PaymentMethod> findByName(String name);

  List<PaymentMethod> findByIsActiveTrue();

  boolean existsByName(String name);
}
