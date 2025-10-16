package com.example.rbac.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "data_apps")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataApp {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
