package com.glamora_store.repository;

import com.glamora_store.entity.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {

    @Query("SELECT DISTINCT a FROM Attribute a LEFT JOIN FETCH a.values ORDER BY a.name")
    List<Attribute> findAllWithValues();
}
