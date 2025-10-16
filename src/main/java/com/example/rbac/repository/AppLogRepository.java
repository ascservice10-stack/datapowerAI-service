package com.example.rbac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.example.rbac.model.AppLog;

public interface AppLogRepository extends JpaRepository<AppLog, Long> {
    Optional<AppLog> findById(Long id);
}
