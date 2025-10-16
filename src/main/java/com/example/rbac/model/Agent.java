package com.example.rbac.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "agents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private String metadata;

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;
}
