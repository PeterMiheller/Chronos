package com.example.chronos.controller;

import com.example.chronos.model.VacationRequest;
import com.example.chronos.service.VacationRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vacation-requests")
public class VacationRequestController {

    private final VacationRequestService vacationRequestService;

    public VacationRequestController(VacationRequestService vacationRequestService) {
        this.vacationRequestService = vacationRequestService;
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
    public ResponseEntity<VacationRequest> create(@RequestBody VacationRequest request) {
        return ResponseEntity.ok(vacationRequestService.save(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        vacationRequestService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}