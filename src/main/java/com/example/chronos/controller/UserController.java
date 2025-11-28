package com.example.chronos.controller;

import com.example.chronos.DTO.CreateAdminRequest;
import com.example.chronos.DTO.CreateEmployeeRequest;
import com.example.chronos.DTO.UpdateEmployeeRequest;
import com.example.chronos.DTO.DashboardSummaryResponse;
import com.example.chronos.model.User;
import com.example.chronos.model.UserType;
import com.example.chronos.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        User user = userService.findById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @GetMapping("/employees")
    public ResponseEntity<List<User>> getAllEmployees() {
        return ResponseEntity.ok(userService.findAllEmployees());
    }

    @GetMapping("/employee/{companyId}")
    public ResponseEntity<List<User>> getEmployeesByCompanyId(@PathVariable int companyId) {
        return ResponseEntity.ok(userService.findEmployeesByCompanyId(companyId));
    }

    @PostMapping("/employee/create")
    public ResponseEntity<?> createEmployee(@RequestBody CreateEmployeeRequest request) {
        try {
            // Check if email already exists
            User existingUser = userService.findByEmail(request.getEmail());
            if (existingUser != null) {
                return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                    put("error", "Email already exists");
                    put("email", request.getEmail());
                }});
            }

            User savedUser = userService.createEmployee(request);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                put("error", e.getMessage());
            }});
        }
    }

    @PutMapping("/employee/update/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable int id, @RequestBody UpdateEmployeeRequest request) {
        try {
            // Check if employee exists
            User existingEmployee = userService.findById(id);
            if (existingEmployee == null) {
                return ResponseEntity.notFound().build();
            }

            // Check if email is being changed to one that already exists
            if (request.getEmail() != null && !request.getEmail().equals(existingEmployee.getEmail())) {
                User userWithEmail = userService.findByEmail(request.getEmail());
                if (userWithEmail != null) {
                    return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                        put("error", "Email already exists");
                        put("email", request.getEmail());
                    }});
                }
            }

            User updatedUser = userService.updateEmployee(id, request);
            return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                put("error", e.getMessage());
            }});
        }
    }

    @DeleteMapping("/employee/delete/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable int id) {
        userService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/administrators")
    public ResponseEntity<List<User>> getAllAdministrators() {
        return ResponseEntity.ok(userService.findAvailableAdministrators());
    }

    @GetMapping("/superadmins")
    public ResponseEntity<List<User>> getAllSuperAdmins() {
        return ResponseEntity.ok(userService.findAllSuperAdmins());
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<User>> getUsersByCompany(@PathVariable int companyId) {
        return ResponseEntity.ok(userService.findByCompanyId(companyId));
    }

    @GetMapping("/administrator/{administratorId}/employees")
    public ResponseEntity<List<User>> getEmployeesByAdministrator(@PathVariable int administratorId) {
        return ResponseEntity.ok(userService.findEmployeesByAdministratorId(administratorId));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/admin")
    public ResponseEntity<User> createAdmin(@RequestBody CreateAdminRequest req) {
        User admin = userService.createAdmin(req);
        return ResponseEntity.ok(admin);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user) {
        User existingUser = userService.findById(id);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }
        user.setId(id);
        return ResponseEntity.ok(userService.save(user));
    }

    @PutMapping("/{id}/vacation-days")
    public ResponseEntity<User> updateVacationDays(
            @PathVariable int id,
            @RequestParam int total,
            @RequestParam int remaining) {
        User user = userService.updateEmployeeVacationDays(id, total, remaining);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/workload")
    public ResponseEntity<User> updateWorkload(
            @PathVariable int id,
            @RequestParam float workload) {
        User user = userService.updateEmployeeWorkload(id, workload);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{employeeId}/administrator/{administratorId}")
    public ResponseEntity<User> assignAdministrator(
            @PathVariable int employeeId,
            @PathVariable int administratorId) {
        User user = userService.assignAdministratorToEmployee(employeeId, administratorId);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/dashboard-summary")
    public ResponseEntity<?> getDashboardSummary(@PathVariable int id, Authentication authentication) {
        try {
            User user = userService.findById(id);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            String currentUserEmail = authentication.getName();
            User currentUser = userService.findByEmail(currentUserEmail);
            
            if (currentUser == null) {
                return ResponseEntity.status(403).body(new HashMap<String, String>() {{
                    put("error", "User not found");
                }});
            }

            // Allow if: user viewing own data, or user is ADMINISTRATOR/SUPERADMIN
            boolean isOwnData = currentUser.getId() == id;
            boolean isAdmin = currentUser.getUserType() == UserType.ADMINISTRATOR || 
                             currentUser.getUserType() == UserType.SUPERADMIN;
            
            if (!isOwnData && !isAdmin) {
                return ResponseEntity.status(403).body(new HashMap<String, String>() {{
                    put("error", "Access denied");
                }});
            }

            Integer vacationTotal = user.getVacationDaysTotal() != null ? user.getVacationDaysTotal() : 0;
            Integer vacationRemaining = user.getVacationDaysRemaining() != null ? user.getVacationDaysRemaining() : 0;
            Integer vacationUsed = vacationTotal - vacationRemaining;

            DashboardSummaryResponse.VacationDaysSummary vacationSummary = 
                new DashboardSummaryResponse.VacationDaysSummary(vacationTotal, vacationUsed, vacationRemaining);

            Float expectedWorkload = user.getExpectedWorkload() != null ? user.getExpectedWorkload() : 40f;
            Float weeklyTarget = expectedWorkload;
            Float hoursThisWeek = 0f;

            DashboardSummaryResponse.WeeklyHoursSummary weeklySummary = 
                new DashboardSummaryResponse.WeeklyHoursSummary(hoursThisWeek, weeklyTarget);

            Integer pendingRequests = 0;

            DashboardSummaryResponse response = new DashboardSummaryResponse(
                    user,
                    vacationSummary,
                    weeklySummary,
                    pendingRequests
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                put("error", e.getMessage());
            }});
        }
    }
}