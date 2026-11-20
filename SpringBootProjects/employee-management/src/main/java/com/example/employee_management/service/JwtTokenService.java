package com.example.employee_management.service;

import java.time.Duration;
import java.time.Instant;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

    private static final Duration TOKEN_LIFETIME = Duration.ofHours(1);

    private final JwtEncoder jwtEncoder;

    public JwtTokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String createToken(Authentication authentication) {
        Instant issuedAt = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("employee-management")
                .issuedAt(issuedAt)
                .expiresAt(issuedAt.plus(TOKEN_LIFETIME))
                .subject(authentication.getName())
                .claim("roles", authentication.getAuthorities().stream()
                        .map(authority -> authority.getAuthority())
                        .toList())
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public long expiresInSeconds() {
        return TOKEN_LIFETIME.toSeconds();
    }
}
