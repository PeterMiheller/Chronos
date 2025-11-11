package com.example.chronos.repository;

import com.example.chronos.model.VacationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VacationRequestRepository extends JpaRepository<VacationRequest, Integer> {
    List<VacationRequest> findByEmployeeId(int employeeId);
    List<VacationRequest> findByAdministratorId(int administratorId);
}