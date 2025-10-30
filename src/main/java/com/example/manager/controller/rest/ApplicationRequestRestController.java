package com.example.manager.controller.rest;

import com.example.manager.model.ApplicationRequest;
import com.example.manager.service.ApplicationRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class ApplicationRequestRestController {

    private final ApplicationRequestService service;

    @GetMapping
    public List<ApplicationRequest> getAllRequests() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationRequest> getRequestById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApplicationRequest> createRequest(@RequestBody ApplicationRequest request) {
        request.setHandled(false);
        ApplicationRequest saved = service.save(request);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationRequest> updateRequest(@PathVariable Long id, @RequestBody ApplicationRequest request) {
        if (!service.getById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        request.setId(id);
        ApplicationRequest updated = service.save(request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        if (!service.getById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}