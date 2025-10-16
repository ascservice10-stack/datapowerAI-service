package com.example.rbac.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProjectCreateDto {
    @NotBlank
    private String name;

    private String description;
}
