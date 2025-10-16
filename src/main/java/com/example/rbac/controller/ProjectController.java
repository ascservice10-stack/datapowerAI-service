package com.example.rbac.controller;

import com.example.rbac.dto.*;
import com.example.rbac.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@Valid @RequestBody ProjectCreateDto dto) {
        return ResponseEntity.ok(projectService.createProject(dto));
    }

    @PostMapping("/{projectId}/assign")
    public ResponseEntity<ProjectMemberDto> assignUser(@PathVariable("projectId") Long projectId, @Valid @RequestBody AssignUserDto dto) {
        return ResponseEntity.ok(projectService.assignUserToProject(projectId, dto));
    }

    @GetMapping
    public ResponseEntity<List<ProjectDto>> listMyProjects() {
        return ResponseEntity.ok(projectService.listProjectsForCurrentUser());
    }

    @GetMapping("/{projectId}/members")
    public ResponseEntity<List<ProjectMemberDto>> members(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(projectService.getProjectMembers(projectId));
    }

    @PutMapping("/{projectId}/name")
    public ResponseEntity<String> updateProjectName(
            @PathVariable("projectId") Long projectId,
            @RequestParam("newName") String newName) {

        projectService.updateProjectName(projectId, newName);
        return ResponseEntity.ok("Project name updated successfully");
    }

}
