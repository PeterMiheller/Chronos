package com.example.chronos.controller;

import com.example.chronos.DTO.VacationRequestDTO;
import com.example.chronos.model.User;
import com.example.chronos.model.VacationRequest;
import com.example.chronos.model.User; // Required Import
import com.example.chronos.model.VacationStatus; // Required Import for the Enum
import com.example.chronos.service.UserService;
import com.example.chronos.service.VacationRequestService;
import com.example.chronos.DTO.VacationStatusUpdateRequest;
import org.springframework.http.HttpHeaders; // Required Import for custom headers
import org.springframework.http.HttpStatus; // Required Import for HTTP status codes
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // Required Import for JWT/User context
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vacation-requests")
public class VacationRequestController {

    private final VacationRequestService vacationRequestService;
    private final UserService userService;

    public VacationRequestController(VacationRequestService vacationRequestService,  UserService userService) {
        this.vacationRequestService = vacationRequestService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<VacationRequest>> getAll() {
        return ResponseEntity.ok(vacationRequestService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VacationRequest> getById(@PathVariable int id) {
        VacationRequest request = vacationRequestService.findById(id);
        return request != null ? ResponseEntity.ok(request) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createRequest(@AuthenticationPrincipal User user, @RequestBody VacationRequestDTO dto) {
        try {
            VacationRequest request = vacationRequestService.create(user, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(request);
        } catch (IllegalArgumentException e) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("error-message", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), headers, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("error-message", "An unexpected error occurred: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        vacationRequestService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<VacationRequest>> getVacationRequestsByEmployee(@PathVariable int employeeId) {
        return ResponseEntity.ok(vacationRequestService.findByEmployeeId(employeeId));
    }

    @GetMapping("/employee/adminId/{employeeId}")
    public ResponseEntity<Integer> getAdminIdByEmployeeId(@PathVariable int employeeId) {
        return ResponseEntity.ok(vacationRequestService.getAdminIdByEmployeeId(employeeId));
    }

    /**
     * Endpoint to get all vacation requests assigned to the authenticated administrator.
     */
    @GetMapping("/administrator/{id}")
    public ResponseEntity<List<VacationRequest>> getVacationRequestsByAdministrator(
            @PathVariable int id,
            Authentication authentication) {

        User admin = userService.findByEmail(authentication.getName());
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Optional: Ensure the logged-in admin matches the requested ID (or is SuperAdmin)
        // if (admin.getId() != id && admin.getUserType() != UserType.SUPERADMIN) {
        //    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        // }

        return ResponseEntity.ok(vacationRequestService.findByAdministratorId(id));
    }


    /**
     * Endpoint to approve or reject a SUBMITTED vacation request.
     */
    @PutMapping("/{requestId}/status")
    public ResponseEntity<VacationRequest> updateVacationRequestStatus(
            @PathVariable int requestId,
            @RequestBody VacationStatusUpdateRequest request,
            Authentication authentication) {

        // 1. Get logged-in administrator ID
        User admin = userService.findByEmail(authentication.getName());
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // Convert string status from DTO to Enum. The DTO must return a String here.
            VacationStatus newStatus = VacationStatus.valueOf(request.getStatus());

            VacationRequest updatedRequest = vacationRequestService.updateVacationRequestStatus(
                    requestId,
                    newStatus,
                    admin.getId() // Pass the administrator ID for service layer check
            );
            return ResponseEntity.ok(updatedRequest);
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage();

            // Convert specific business logic error to a friendly message
            if ("INSUFFICIENT_DAYS".equals(errorMessage)) {
                errorMessage = "Employee does not have enough remaining vacation days to approve this request.";
            } else if (errorMessage.startsWith("No enum constant")) {
                errorMessage = "Invalid status provided. Allowed statuses: APPROVED, REJECTED.";
            }

            // Pass the error message back in a custom header
            HttpHeaders headers = new HttpHeaders();
            headers.add("error-message", errorMessage);

            return new ResponseEntity<>(null, headers, HttpStatus.BAD_REQUEST); // 400 Bad Request
        } catch (SecurityException e) {
            // Handle authorization failure (wrong administrator)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        } catch (IllegalStateException e) {
            // Handle state transition failure (e.g., request already processed)
            HttpHeaders headers = new HttpHeaders();
            headers.add("error-message", e.getMessage());
            return new ResponseEntity<>(null, headers, HttpStatus.CONFLICT); // 409 Conflict
        } catch (Exception e) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("error-message", "An unexpected error occurred during processing.");
            return new ResponseEntity<>(null, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable int id, Authentication authentication) {
        try {
            VacationRequest request = vacationRequestService.findById(id);
            if (request == null) {
                return ResponseEntity.notFound().build();
            }

            // Security Check
            User currentUser = userService.findByEmail(authentication.getName());
            boolean isOwner = currentUser.getId() == request.getEmployeeId();
            boolean isAdmin = currentUser.getId() == request.getAdministratorId();
            boolean isSuperAdmin = currentUser.getUserType().name().equals("SUPERADMIN");

            if (!isOwner && !isAdmin && !isSuperAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            byte[] pdfBytes = vacationRequestService.generatePdf(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "vacation_request_" + id + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}