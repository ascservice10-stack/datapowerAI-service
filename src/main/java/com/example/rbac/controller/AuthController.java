package com.example.rbac.controller;

import com.example.rbac.dto.AuthResponse;
import com.example.rbac.dto.LoginRequest;
import com.example.rbac.dto.RegisterRequest;
import com.example.rbac.model.Role;
import com.example.rbac.model.User;
import com.example.rbac.repository.RoleRepository;
import com.example.rbac.repository.UserRepository;
import com.example.rbac.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        if (userRepo.findByUsername(req.username()).isPresent())
            return ResponseEntity.badRequest().body("Username already exists");
        var user = User.builder()
                .username(req.username())
                .password(passwordEncoder.encode(req.password()))
                .email(req.email())
                .build();
        User finalUser = user;
        roleRepo.findByName("ROLE_USER").ifPresent(r -> finalUser.getRoles().add(r));
        userRepo.save(user);
        return ResponseEntity.ok("User created");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        var opt = userRepo.findByUsername(req.username());
        if (opt.isEmpty()) return ResponseEntity.status(401).body("Invalid credentials");
        var user = opt.get();
        if (!passwordEncoder.matches(req.password(), user.getPassword())) return ResponseEntity.status(401).body("Invalid credentials");

        String rolesCsv = user.getRoles().stream().map(Role::getName).collect(Collectors.joining(","));
        String token = jwtTokenProvider.createToken(user.getUsername(), rolesCsv);
        return ResponseEntity.ok(new AuthResponse(token, "Bearer", 3600));
    }

    @GetMapping("/sso/success")
    public ResponseEntity<?> ssoSuccess(@AuthenticationPrincipal OidcUser oidcUser) {
        String username = oidcUser.getPreferredUsername() != null ? oidcUser.getPreferredUsername() : oidcUser.getEmail();
        var user = userRepo.findByUsername(username).orElseGet(() -> {
            User u = User.builder().username(username).email(oidcUser.getEmail()).password("").build();
            roleRepo.findByName("ROLE_USER").ifPresent(r -> u.getRoles().add(r));
            return userRepo.save(u);
        });
        String rolesCsv = user.getRoles().stream().map(Role::getName).collect(Collectors.joining(","));
        String token = jwtTokenProvider.createToken(user.getUsername(), rolesCsv);
        return ResponseEntity.ok(new AuthResponse(token, "Bearer", 3600));
    }
}
