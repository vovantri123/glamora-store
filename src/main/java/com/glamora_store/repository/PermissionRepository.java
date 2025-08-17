package com.glamora_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.glamora_store.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {}
