package com.example.manager.service;

import com.example.manager.model.ApplicationRequest;
import com.example.manager.model.Operator;
import com.example.manager.repository.ApplicationRequestRepository;
import com.example.manager.repository.CourseRepository;
import com.example.manager.repository.OperatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
@RequiredArgsConstructor
public class ApplicationRequestService {

    private final ApplicationRequestRepository repo;
    private final OperatorRepository operatorRepo;
    private final CourseRepository courseRepo; // Добавьте эту строку

    public List<ApplicationRequest> getAll() {
        return repo.findAll();
    }

    public List<ApplicationRequest> getPending() {
        return repo.findByHandledFalse();
    }

    public List<ApplicationRequest> getProcessed() {
        return repo.findByHandledTrue();
    }

    public Optional<ApplicationRequest> getById(Long id) {
        return repo.findById(id);
    }

    public ApplicationRequest save(ApplicationRequest request) {
        return repo.save(request);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public void process(Long id) {
        repo.findById(id).ifPresent(r -> {
            r.setHandled(true);
            repo.save(r);
        });
    }

    public void assignOperators(Long requestId, List<Long> operatorIds) {
        repo.findById(requestId).ifPresent(r -> {
            if (r.isHandled()) {
                return;
            }
            Set<Operator> ops = new HashSet<>(operatorRepo.findAllById(operatorIds));
            r.setOperators(ops);
            if (!ops.isEmpty()) {
                r.setHandled(true);
            }
            repo.save(r);
        });
    }

    public void removeOperator(Long requestId, Long operatorId) {
        repo.findById(requestId).ifPresent(r -> {
            r.getOperators().removeIf(op -> Objects.equals(op.getId(), operatorId));
            repo.save(r);
        });
    }

    public ApplicationRequest saveWithCourse(ApplicationRequest request, Long courseId) {
        courseRepo.findById(courseId).ifPresent(request::setCourse);
        return repo.save(request);
    }

    public ApplicationRequest updateWithCourse(ApplicationRequest request, Long courseId) {
        courseRepo.findById(courseId).ifPresent(request::setCourse);
        return repo.save(request);
    }
}