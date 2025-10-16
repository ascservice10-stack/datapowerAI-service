package com.example.rbac.config;

import com.example.rbac.model.Role;
import com.example.rbac.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final RoleRepository roleRepo;

    @Bean
    CommandLineRunner initRoles() {
        return args -> {
            createRoleIfMissing("TOOL_ADMIN", "System wide admin");
            createRoleIfMissing("PROJECT_ADMIN", "Admin for a project");
            createRoleIfMissing("USER", "Standard user");
            createRoleIfMissing("READ_ONLY", "Read only access");
        };
    }

    private void createRoleIfMissing(String name, String desc) {
        roleRepo.findByName(name).orElseGet(() -> roleRepo.save(Role.builder().name(name).description(desc).build()));
    }
}
