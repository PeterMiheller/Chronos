package com.example.chronos.service;

import com.example.chronos.model.VacationRequest;
import com.example.chronos.repository.VacationRequestRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VacationRequestService {
    private final VacationRequestRepository repository;

    public VacationRequestService(VacationRequestRepository repository) { this.repository = repository; }

    public VacationRequest save(VacationRequest request) { return repository.save(request); }
    public List<VacationRequest> findAll() { return repository.findAll(); }
    public VacationRequest findById(int id) { return repository.findById(id).orElse(null); }
    public void deleteById(int id) { repository.deleteById(id); }
}
