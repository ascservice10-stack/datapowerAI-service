package com.example.rbac.dto;

public record AuthResponse(String token, String tokenType, long expiresIn) {}
