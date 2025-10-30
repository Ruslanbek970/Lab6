package com.example.manager.repository;

import com.example.manager.model.ApplicationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRequestRepository extends JpaRepository<ApplicationRequest, Long> {
    List<ApplicationRequest> findByHandledFalse();
    List<ApplicationRequest> findByHandledTrue();
}
