package com.example.rbac.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(
    @NotBlank String username,
    @NotBlank @Size(min = 6) String password,
    @Email String email
) {}
