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
    public VacationRequest createRequest(@AuthenticationPrincipal User user, @RequestBody VacationRequestDTO dto) {
    return vacationRequestService.create(user, dto);
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
     * The administrator ID is retrieved from the JWT token via the Authentication object.
     */
    @GetMapping("/administrator")
    public ResponseEntity<List<VacationRequest>> getVacationRequestsByAdministrator(Authentication authentication) {
        // 1. Get logged-in user (administrator)
        User admin = userService.findByEmail(authentication.getName());
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 2. Fetch requests assigned to this administrator's ID
        return ResponseEntity.ok(vacationRequestService.findByAdministratorId(admin.getId()));
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
}