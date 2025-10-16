package com.example.rbac.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ui_submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UiSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "page_id")
    private UiPage page;

    private LocalDateTime submittedAt = LocalDateTime.now();
}

