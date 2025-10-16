package com.example.rbac.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ui_components")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UiComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "page_id")
    private UiPage page;

    private String type;
    private String label;
    private String keyName;
    private Boolean required;
    private Integer orderIndex;

    @Column(columnDefinition = "jsonb")
    private String config; // JSON string for dynamic options
}

