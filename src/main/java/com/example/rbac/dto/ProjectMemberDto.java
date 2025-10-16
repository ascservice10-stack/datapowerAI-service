package com.example.rbac.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectMemberDto {
    private Long userId;
    private String username;
    private Long roleId;
    private String roleName;
}
