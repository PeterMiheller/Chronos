package com.example.chronos.repository;

import com.example.chronos.model.VacationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacationRequestRepository extends JpaRepository<VacationRequest, Integer> {}
