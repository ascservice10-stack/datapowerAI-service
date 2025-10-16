package com.example.rbac.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ui_values")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UiComponentValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "submission_id")
    private UiSubmission submission;

    @ManyToOne
    @JoinColumn(name = "component_id")
    private UiComponent component;

    @Column(columnDefinition = "jsonb")
    private String value; // can be stringified JSON
}

