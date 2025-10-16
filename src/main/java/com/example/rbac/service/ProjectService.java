package com.example.rbac.service;

import com.example.rbac.dto.*;
import com.example.rbac.exception.UnauthorizedException;
import com.example.rbac.model.*;
import com.example.rbac.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepo;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final ProjectAssignmentRepository assignmentRepo;

    // helper: get currently authenticated username
    private String currentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) return null;
        Object p = auth.getPrincipal();
        if (p instanceof org.springframework.security.core.userdetails.UserDetails ud) {
            return ud.getUsername();
        }
        // If using OidcUser for SSO
        if (p instanceof org.springframework.security.oauth2.core.oidc.user.OidcUser oidc) {
            return oidc.getPreferredUsername() != null ? oidc.getPreferredUsername() : oidc.getEmail();
        }
        if (p instanceof String) { // sometimes principal is username string
            return (String) p;
        }
        return null;
    }

    // check if current user has global TOOL_ADMIN
    private boolean isToolAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TOOL_ADMIN"));
    }

    // create project: must be TOOL_ADMIN or any authenticated user (owner becomes current user)
    @Transactional
    public ProjectDto createProject(ProjectCreateDto dto) {
        String username = currentUsername();
        User owner = userRepo.findByUsername(username).orElseThrow(() -> new IllegalStateException("Current user not found"));

        Project p = Project.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .owner(owner)
                .build();

        Project saved = projectRepo.save(p);

        // auto-assign owner as PROJECT_ADMIN for that project
        Role projAdminRole = roleRepo.findByName("PROJECT_ADMIN")
                .orElseThrow(() -> new IllegalStateException("PROJECT_ADMIN role missing"));
        ProjectAssignment pa = ProjectAssignment.builder()
                .project(saved)
                .user(owner)
                .role(projAdminRole)
                .build();
        assignmentRepo.save(pa);

        return ProjectDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .description(saved.getDescription())
                .ownerUsername(owner.getUsername())
                .build();
    }

    // assign a user to a project with a specific role
    @Transactional
    public ProjectMemberDto assignUserToProject(Long projectId, AssignUserDto dto) {
        Project proj = projectRepo.findById(projectId).orElseThrow(() -> new NoSuchElementException("Project not found"));
        User userToAssign = userRepo.findById(dto.getUserId()).orElseThrow(() -> new NoSuchElementException("User not found"));
        Role role = roleRepo.findById(dto.getRoleId()).orElseThrow(() -> new NoSuchElementException("Role not found"));

        // authorization: only TOOL_ADMIN, project owner, or project admin can assign
        if (!isToolAdmin()) {
            String username = currentUsername();
            if (username == null) throw new SecurityException("Not authenticated");
            User current = userRepo.findByUsername(username).orElseThrow();

            boolean isOwner = proj.getOwner() != null && proj.getOwner().getId().equals(current.getId());
            boolean isProjectAdmin = assignmentRepo.findByProjectAndUser(proj, current)
                    .map(a -> a.getRole().getName().equals("PROJECT_ADMIN")).orElse(false);

            if (!isOwner && !isProjectAdmin) {
                throw new SecurityException("Not allowed to assign users for this project");
            }
        }

        // Update existing assignment or create new
        Optional<ProjectAssignment> existing = assignmentRepo.findByProjectAndUser(proj, userToAssign);
        ProjectAssignment pa = existing.orElseGet(() -> ProjectAssignment.builder().project(proj).user(userToAssign).build());
        pa.setRole(role);
        assignmentRepo.save(pa);

        return ProjectMemberDto.builder()
                .userId(userToAssign.getId())
                .username(userToAssign.getUsername())
                .roleId(role.getId())
                .roleName(role.getName())
                .build();
    }

    public List<ProjectDto> listProjectsForCurrentUser() {
        String username = currentUsername();
        if (username == null) return Collections.emptyList();

        if (isToolAdmin()) {
            return projectRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
        }

        User user = userRepo.findByUsername(username).orElseThrow();
        List<Project> direct = assignmentRepo.findByUser(user).stream()
                .map(ProjectAssignment::getProject)
                .distinct().collect(Collectors.toList());

        // also include projects owned by the user
        direct.addAll(projectRepo.findByOwner(user));
        return direct.stream().distinct().map(this::toDto).collect(Collectors.toList());
    }

    public List<ProjectMemberDto> getProjectMembers(Long projectId) {
        Project proj = projectRepo.findById(projectId).orElseThrow();
        return assignmentRepo.findByProject(proj).stream().map(a ->
                ProjectMemberDto.builder()
                        .userId(a.getUser().getId())
                        .username(a.getUser().getUsername())
                        .roleId(a.getRole().getId())
                        .roleName(a.getRole().getName())
                        .build()
        ).collect(Collectors.toList());
    }

    public void updateProjectName(Long projectId, String newName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated");
        }

        String username = authentication.getName(); // works for both SSO and JWT

        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Now fetch your user entity from DB
        User currentUser = userRepo.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("TOOL_ADMIN"));

        boolean isOwner = project.getOwner() != null &&
                project.getOwner().getUsername().equals(username);

        if (!isAdmin && !isOwner) {
            throw new UnauthorizedException("You are not authorized to update this project");
        }

        project.setName(newName);
        projectRepo.save(project);
    }

    private ProjectDto toDto(Project p) {
        return ProjectDto.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .ownerUsername(p.getOwner() == null ? null : p.getOwner().getUsername())
                .build();
    }
}
