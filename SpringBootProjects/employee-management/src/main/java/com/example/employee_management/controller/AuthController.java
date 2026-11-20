package com.example.employee_management.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import com.example.employee_management.dto.AuthResponse;
import com.example.employee_management.dto.LoginRequest;
import com.example.employee_management.dto.RegisterRequest;
import com.example.employee_management.model.User;
import com.example.employee_management.service.JwtTokenService;
import com.example.employee_management.service.UserService;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService,
            JwtTokenService jwtTokenService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request);
        Authentication authentication = authenticate(user.username(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(authentication));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticate(userService.normalizeUsername(request.username()),
                request.password());
        return ResponseEntity.ok(toResponse(authentication));
    }

    private Authentication authenticate(String username, String password) {
        try {
            return authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(username, password));
        } catch (AuthenticationException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
    }

    private AuthResponse toResponse(Authentication authentication) {
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .map(authority -> authority.substring("ROLE_".length()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Authenticated user has no role"));
        return new AuthResponse(jwtTokenService.createToken(authentication), "Bearer",
                jwtTokenService.expiresInSeconds(), authentication.getName(), role);
    }
}
