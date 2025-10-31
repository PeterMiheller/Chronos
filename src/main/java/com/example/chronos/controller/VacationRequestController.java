package com.example.chronos.controller;

import com.example.chronos.model.VacationRequest;
import com.example.chronos.service.VacationRequestService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/vacationrequests")
public class VacationRequestController {

    private final VacationRequestService service;

    public VacationRequestController(VacationRequestService service) { this.service = service; }

    @GetMapping
    public List<VacationRequest> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public VacationRequest getById(@PathVariable int id) { return service.findById(id); }

    @PostMapping
    public VacationRequest create(@RequestBody VacationRequest request) { return service.save(request); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) { service.deleteById(id); }
}
