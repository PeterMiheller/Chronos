package com.example.chronos.service;

import com.example.chronos.model.VacationRequest;
import com.example.chronos.model.User;
import com.example.chronos.DTO.VacationRequestDTO;
import com.example.chronos.model.VacationStatus;
import com.example.chronos.repository.UserRepository;
import com.example.chronos.repository.VacationRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class VacationRequestService {
    private final VacationRequestRepository vacationRequestRepository;
    private final UserRepository userRepository;

    public VacationRequestService(VacationRequestRepository vacationRequestRepository,UserRepository userRepository) {
        this.vacationRequestRepository = vacationRequestRepository;
        this.userRepository = userRepository;
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

    public List<VacationRequest> findByEmployeeId(int employeeId) {
        return vacationRequestRepository.findByEmployeeId(employeeId);
    }

    public List<VacationRequest> findByAdministratorId(int administratorId) {
        return vacationRequestRepository.findByAdministratorId(administratorId);
    }

    public VacationRequest create(User user, VacationRequestDTO dto) {

        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new RuntimeException("Start date cannot be after end date");
        }

        VacationRequest req = new VacationRequest();
        req.setEmployeeId(user.getId());
        req.setAdministratorId(user.getAdministratorId());
        req.setStartDate(dto.getStartDate());
        req.setEndDate(dto.getEndDate());
        req.setStatus(VacationStatus.PENDING);
        req.setPdfPath(null);

        return vacationRequestRepository.save(req);
    }





}