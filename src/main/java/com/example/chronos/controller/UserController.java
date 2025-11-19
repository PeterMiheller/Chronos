package com.example.chronos.controller;

import com.example.chronos.DTO.CreateAdminRequest;
import com.example.chronos.model.User;
import com.example.chronos.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/administrators")
    public ResponseEntity<List<User>> getAllAdministrators() {
        return ResponseEntity.ok(userService.findAllAdministrators());
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
}