package com.example.rbac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.example.rbac.model.Agent;

public interface AgentRepository extends JpaRepository<Agent, Long> {
    Optional<Agent> findById(Long id);
}
