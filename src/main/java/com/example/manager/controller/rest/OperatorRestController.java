package com.example.manager.controller.rest;

import com.example.manager.model.Operator;
import com.example.manager.repository.OperatorRepository;
import com.example.manager.service.ApplicationRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operators")
@RequiredArgsConstructor
public class OperatorRestController {

    private final OperatorRepository operatorRepository;
    private final ApplicationRequestService applicationRequestService;

    @GetMapping
    public List<Operator> getAllOperators() {
        return operatorRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Operator> createOperator(@RequestBody Operator operator) {
        Operator saved = operatorRepository.save(operator);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{operatorId}/assign/{requestId}")
    public ResponseEntity<String> assignOperator(@PathVariable Long operatorId, @PathVariable Long requestId) {
        if (!operatorRepository.existsById(operatorId) || !applicationRequestService.getById(requestId).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        applicationRequestService.assignOperators(requestId, List.of(operatorId));
        return ResponseEntity.ok("Operator assigned successfully");
    }


    @DeleteMapping("/{operatorId}/remove/{requestId}")
    public ResponseEntity<String> removeOperatorFromRequest(@PathVariable Long operatorId, @PathVariable Long requestId) {
        if (!operatorRepository.existsById(operatorId) || !applicationRequestService.getById(requestId).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        applicationRequestService.removeOperator(requestId, operatorId);
        return ResponseEntity.ok("Operator removed from request successfully");
    }
}