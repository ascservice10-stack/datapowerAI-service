package com.example.rbac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.example.rbac.model.DataApp;

public interface DataAppRepository extends JpaRepository<DataApp, Long> {
    Optional<DataApp> findById(Long id);
}
