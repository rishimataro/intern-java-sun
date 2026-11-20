package com.example.employee_management.component;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.employee_management.model.User;
import com.example.employee_management.model.UserRole;
import com.example.employee_management.repository.UserRepository;

@Configuration(proxyBeanMethods = false)
public class AdminUserInitializer {

    @Bean
    public ApplicationRunner initializeAdminUser(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.security.admin.username:}") String configuredUsername,
            @Value("${app.security.admin.password:}") String configuredPassword) {
        return arguments -> {
            boolean usernameProvided = configuredUsername != null && !configuredUsername.isBlank();
            boolean passwordProvided = configuredPassword != null && !configuredPassword.isBlank();
            if (usernameProvided != passwordProvided) {
                throw new IllegalStateException("Set both ADMIN_USERNAME and ADMIN_PASSWORD to create an admin user");
            }
            if (!usernameProvided) {
                return;
            }

            String username = configuredUsername.trim().toLowerCase(Locale.ROOT);
            if (configuredPassword.length() < 8
                    || configuredPassword.getBytes(StandardCharsets.UTF_8).length > 72) {
                throw new IllegalStateException("ADMIN_PASSWORD must be 8 to 72 UTF-8 bytes long");
            }
            User existing = userRepository.findByUsername(username).orElse(null);
            if (existing != null) {
                if (existing.role() != UserRole.ADMIN) {
                    throw new IllegalStateException("Configured admin username already belongs to a non-admin user");
                }
                return;
            }
            userRepository.save(new User(username, passwordEncoder.encode(configuredPassword), UserRole.ADMIN));
        };
    }
}
