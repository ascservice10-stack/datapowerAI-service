package com.example.rbac.repository;

import com.example.rbac.model.Project;
import com.example.rbac.model.ProjectAssignment;
import com.example.rbac.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectAssignmentRepository extends JpaRepository<ProjectAssignment, Long> {
    List<ProjectAssignment> findByProject(Project project);
    List<ProjectAssignment> findByUser(User user);
    Optional<ProjectAssignment> findByProjectAndUser(Project project, User user);
}

