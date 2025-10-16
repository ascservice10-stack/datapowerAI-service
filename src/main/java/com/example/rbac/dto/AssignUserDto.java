package com.example.rbac.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignUserDto {
    @NotNull
    private Long userId;
    @NotNull
    private Long roleId;
}
