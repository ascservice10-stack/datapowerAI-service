package com.example.rbac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.example.rbac.model.Workspace;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    Optional<Workspace> findById(Long id);
}
