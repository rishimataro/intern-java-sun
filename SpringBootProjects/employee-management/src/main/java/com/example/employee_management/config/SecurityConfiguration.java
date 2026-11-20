package com.example.employee_management.config;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Locale;

import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.example.employee_management.model.User;
import com.example.employee_management.repository.UserRepository;
import com.example.employee_management.security.AuthEndpointMatcher;
import com.nimbusds.jose.jwk.source.ImmutableSecret;

@Configuration(proxyBeanMethods = false)
public class SecurityConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            AuthEndpointMatcher authEndpointMatcher,
            Converter<Jwt, ? extends org.springframework.security.authentication.AbstractAuthenticationToken> jwtConverter)
            throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(authEndpointMatcher).permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/employees").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/employees/list", "/employees/search")
                        .hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/**").hasRole("ADMIN")
                        .requestMatchers("/employees/add", "/employees/add/**", "/employees/code").hasRole("ADMIN")
                        .requestMatchers("/actuator/health").permitAll()
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.ignoringRequestMatchers(authEndpointMatcher))
                .httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer(resourceServer -> resourceServer
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter)));
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("roles");
        authoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByUsername(username.toLowerCase(Locale.ROOT))
                .map(SecurityConfiguration::toUserDetails)
                .orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException(
                        "User not found"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecretKey jwtSecretKey(@Value("${app.security.jwt.secret:}") String configuredSecret) {
        if (configuredSecret == null || configuredSecret.isBlank()) {
            LOGGER.warn("JWT_SECRET is not configured; using a random key. Tokens will stop working after restart.");
            return generateRandomSecret();
        }

        byte[] secret = decodeSecret(configuredSecret.trim());
        if (secret.length < 32) {
            throw new IllegalStateException("JWT_SECRET must be at least 32 bytes (or base64 for 32 bytes)");
        }
        return new javax.crypto.spec.SecretKeySpec(secret, "HmacSHA256");
    }

    @Bean
    public JwtEncoder jwtEncoder(SecretKey jwtSecretKey) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtSecretKey));
    }

    @Bean
    public JwtDecoder jwtDecoder(SecretKey jwtSecretKey) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(jwtSecretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
        OAuth2TokenValidator<Jwt> validator = JwtValidators.createDefaultWithIssuer("employee-management");
        decoder.setJwtValidator(validator);
        return decoder;
    }

    private static org.springframework.security.core.userdetails.UserDetails toUserDetails(User user) {
        return org.springframework.security.core.userdetails.User.withUsername(user.username())
                .password(user.password())
                .roles(user.role().name())
                .build();
    }

    private static byte[] decodeSecret(String configuredSecret) {
        if (!configuredSecret.startsWith("base64:")) {
            return configuredSecret.getBytes(StandardCharsets.UTF_8);
        }
        try {
            return Base64.getDecoder().decode(configuredSecret.substring("base64:".length()));
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException("JWT_SECRET has an invalid base64: value", exception);
        }
    }

    private static SecretKey generateRandomSecret() {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("HmacSHA256");
            generator.init(256);
            return generator.generateKey();
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("HmacSHA256 is not available", exception);
        }
    }
}
