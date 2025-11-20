package com.example.chronos.repository;

import com.example.chronos.model.VacationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VacationRequestRepository extends JpaRepository<VacationRequest, Integer> {
    List<VacationRequest> findByEmployeeId(int employeeId);
    List<VacationRequest> findByAdministratorId(int administratorId);

    @Query("""
        SELECT COUNT(v) > 0
        FROM VacationRequest v
        WHERE v.employeeId = :employeeId
          AND v.status <> 'REJECTED'
          AND v.startDate <= :endDate
          AND v.endDate >= :startDate
        """)
    boolean existsOverlap(
            @Param("employeeId") Integer employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


}