package com.example.demo.config;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create Roles if they don't exist
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN");
        Role employeeRole = createRoleIfNotFound("ROLE_EMPLOYEE");

        // Create Admin User if it doesn't exist
        if (userRepository.findByEmail("admin").isEmpty()) {
            User admin = User.builder()
                    .username("Administrator")
                    .email("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role(adminRole)
                    .build();
            userRepository.save(admin);
        }
    }

    private Role createRoleIfNotFound(String roleName) {
        Optional<Role> roleOptional = roleRepository.findByNom(roleName);
        if (roleOptional.isEmpty()) {
            Role role = new Role();
            role.setNom(roleName);
            return roleRepository.save(role);
        }
        return roleOptional.get();
    }
}
