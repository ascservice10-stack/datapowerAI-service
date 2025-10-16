package com.example.rbac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.example.rbac.model.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findById(Long id);
}
