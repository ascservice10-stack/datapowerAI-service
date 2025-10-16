package com.example.rbac.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "project_assignments", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"project_id", "user_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectAssignment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    // role assigned for this project (reference to Role)
    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id")
    private Role role;
}
