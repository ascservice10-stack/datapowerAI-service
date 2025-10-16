package com.example.rbac.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // e.g. TOOL_ADMIN, PROJECT_ADMIN, USER, READ_ONLY
    @Column(unique = true, nullable = false)
    private String name;

    private String description;
}
