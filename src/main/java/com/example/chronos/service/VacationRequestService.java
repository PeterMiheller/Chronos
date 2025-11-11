package com.example.chronos.service;

import com.example.chronos.model.VacationRequest;
import com.example.chronos.repository.VacationRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VacationRequestService {
    private final VacationRequestRepository vacationRequestRepository;

    public VacationRequestService(VacationRequestRepository vacationRequestRepository) {
        this.vacationRequestRepository = vacationRequestRepository;
    }

    public VacationRequest save(VacationRequest request) {
        return vacationRequestRepository.save(request);
    }

    public List<VacationRequest> findAll() {
        return vacationRequestRepository.findAll();
    }

    public VacationRequest findById(int id) {
        return vacationRequestRepository.findById(id).orElse(null);
    }

    public void deleteById(int id) {
        vacationRequestRepository.deleteById(id);
    }
}