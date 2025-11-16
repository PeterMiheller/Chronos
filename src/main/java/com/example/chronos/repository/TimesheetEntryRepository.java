package com.example.chronos.repository;

import com.example.chronos.model.TimesheetEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TimesheetEntryRepository extends JpaRepository<TimesheetEntry, Integer> {
    @Query("SELECT t FROM TimesheetEntry t WHERE t.user.id = :userId AND t.date BETWEEN :start AND :end ORDER BY t.date")
    List<TimesheetEntry> findByUserIdAndDateBetween(@Param("userId") int userId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);

    Optional<TimesheetEntry> findByUserIdAndDate(int userId, LocalDate date);
}
