package com.example.chronos.repository;

import com.example.chronos.model.Admin;
import com.example.chronos.model.SuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SuperAdminRepository extends JpaRepository<SuperAdmin, Integer> {
    Optional<SuperAdmin> findByEmail(String email);
}
