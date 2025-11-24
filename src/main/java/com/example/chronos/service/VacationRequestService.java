package com.example.chronos.service;

import com.example.chronos.DTO.VacationRequestDTO;
import com.example.chronos.model.User;
import com.example.chronos.model.VacationRequest;
import com.example.chronos.model.VacationStatus;
import com.example.chronos.repository.UserRepository;
import com.example.chronos.repository.VacationRequestRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class VacationRequestService {
    private final VacationRequestRepository vacationRequestRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public VacationRequestService(VacationRequestRepository vacationRequestRepository, UserRepository userRepository, UserService userService) {
        this.vacationRequestRepository = vacationRequestRepository;
        this.userRepository = userRepository;
        this.userService = userService;
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

    public VacationRequest updateVacationRequestStatus(int requestId, VacationStatus newStatus, int administratorId) {
        VacationRequest request = vacationRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Vacation request not found with ID: " + requestId));

        if (request.getAdministratorId() != administratorId) {
            throw new SecurityException("User is not the assigned administrator for this request.");
        }

        if (request.getStatus() != VacationStatus.PENDING) {
            throw new IllegalStateException("Request status must be SUBMITTED to be processed.");
        }

        if (newStatus == VacationStatus.APPROVED) {
            User employee = userRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            long requestedDays = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;
            int days = (int) requestedDays;

            if (employee.getVacationDaysRemaining() == null || employee.getVacationDaysRemaining() < days) {
                throw new IllegalArgumentException("INSUFFICIENT_DAYS");
            }

            employee.setVacationDaysRemaining(employee.getVacationDaysRemaining() - days);
            userRepository.save(employee);

            request.setStatus(VacationStatus.APPROVED);
        } else if (newStatus == VacationStatus.REJECTED) {
            request.setStatus(VacationStatus.REJECTED);
        } else {
            throw new IllegalArgumentException("Invalid status update. Only APPROVED or REJECTED is allowed.");
        }

        return vacationRequestRepository.save(request);
    }


    public VacationRequest create(User user, VacationRequestDTO dto) {

        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new IllegalArgumentException("Start and end dates are required.");
        }

        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }

        VacationRequest request = new VacationRequest();
        request.setEmployeeId(user.getId());
        request.setAdministratorId(dto.getAdministratorId());
        request.setStartDate(dto.getStartDate());
        request.setEndDate(dto.getEndDate());
        request.setStatus(VacationStatus.PENDING);

        return vacationRequestRepository.save(request);
    }

    public Integer getAdminIdByEmployeeId(int employeeId) {
        return userService.getAdminId(employeeId);
    }
}
