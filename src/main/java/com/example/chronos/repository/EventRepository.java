package com.example.chronos.repository;

import com.example.chronos.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByCompanyId(int companyId);

    List<Event> findByCompanyIdAndEventDateTimeBetween(int companyId, LocalDateTime start, LocalDateTime end);
}
