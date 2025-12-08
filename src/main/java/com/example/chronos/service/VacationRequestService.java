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
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

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

        List<VacationRequest> overlapping =
                vacationRequestRepository.findOverlappingRequests(
                        user.getId(),
                        dto.getStartDate(),
                        dto.getEndDate()
                );

        if (!overlapping.isEmpty()) {
            throw new IllegalArgumentException("You already have a vacation request in this interval.");
        }

        // Determine the administrator ID based on user type
        Integer administratorId;

        // If the user is an EMPLOYEE, use their assigned administrator
        if (user.getUserType() == com.example.chronos.model.UserType.EMPLOYEE) {
            administratorId = user.getAdministratorId();
            if (administratorId == null) {
                throw new IllegalArgumentException("Employee does not have an assigned administrator.");
            }
        }
        // If the user is ADMINISTRATOR, they approve their own request
        else if (user.getUserType() == com.example.chronos.model.UserType.ADMINISTRATOR) {
            administratorId = user.getId(); // Self-approval for administrators
        }
        // SUPERADMIN cannot create vacation requests
        else if (user.getUserType() == com.example.chronos.model.UserType.SUPERADMIN) {
            throw new IllegalArgumentException("SUPERADMIN users cannot create vacation requests.");
        } else {
            throw new IllegalArgumentException("Invalid user type for vacation request.");
        }

        VacationRequest request = new VacationRequest();
        request.setEmployeeId(user.getId());
        request.setAdministratorId(administratorId);
        request.setStartDate(dto.getStartDate());
        request.setEndDate(dto.getEndDate());
        request.setStatus(VacationStatus.PENDING);

        return vacationRequestRepository.save(request);
    }

    public Integer getAdminIdByEmployeeId(int employeeId) {
        return userService.getAdminId(employeeId);
    }

    public byte[] generatePdf(int requestId) {
        VacationRequest request = findById(requestId);
        if (request == null) {
            throw new RuntimeException("Vacation request not found");
        }

        User employee = userRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        User admin = userRepository.findById(request.getAdministratorId())
                .orElseThrow(() -> new RuntimeException("Administrator not found"));

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Vacation Request Details", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Details
            Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            document.add(new Paragraph("Request ID: " + request.getId(), contentFont));
            document.add(Chunk.NEWLINE);
            
            document.add(new Paragraph("Employee Name: " + employee.getName(), contentFont));
            document.add(new Paragraph("Employee Email: " + employee.getEmail(), contentFont));
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Start Date: " + request.getStartDate().format(formatter), contentFont));
            document.add(new Paragraph("End Date: " + request.getEndDate().format(formatter), contentFont));
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Status: " + request.getStatus(), contentFont));
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Approved/Reviewed By: " + admin.getName(), contentFont));
            document.add(new Paragraph("Administrator Email: " + admin.getEmail(), contentFont));

            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("Generated on: " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10)));

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}
