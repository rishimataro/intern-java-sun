# Module 9: Spring Security Implementation Plan

> **For agentic workers:** Implementation checklist for Lab 9.

**Goal:** Add registration, login, Basic Authentication, JWT authentication, and USER/ADMIN authorization to the employee-management project.

**Architecture:** Persist users in JPA with BCrypt password hashes and string roles. Public register/login endpoints issue one-hour HS256 JWTs; Spring Security accepts both Bearer JWTs and HTTP Basic credentials. Authorization rules allow USER and ADMIN to read while limiting writes to ADMIN.

**Tech Stack:** Spring Boot 4.1.2, Spring Security, OAuth2 Resource Server JWT support, Spring Data JPA, BCrypt.

**Spec:** `docs/lab9.md`

## Global Constraints

- Public registration always creates a USER and never accepts a role field.
- Do not store plaintext passwords or hardcode a signing secret or default administrator password.
- Preserve CSRF for Thymeleaf forms; ignore it for public JSON auth endpoints and Bearer-token requests.
- USER can read employee data; only ADMIN can write employee data.

## Review Focus

- Role escalation during registration — fixed USER role in `UserService`.
- Password disclosure/storage — BCrypt encoding and no password in response DTO.
- Missing or invalid JWT signatures/expiry — Nimbus decoder validates HS256 signature, issuer, and timestamps.
- Browser CSRF on form writes — CSRF remains enabled for Thymeleaf and Basic-authenticated writes.
- Admin provisioning — environment-only bootstrap with paired values and no promotion of an existing USER.
- Request abuse — in-memory bounded per-address rate limits with stricter authentication attempt limits.

---

### Task 1: Persist users and register accounts

**Files:** `model/User.java`, `model/UserRole.java`, `repository/UserRepository.java`, `dto/RegisterRequest.java`, `service/UserService.java`

- [x] Store unique usernames, BCrypt password hashes, and USER/ADMIN roles.
- [x] Validate and normalize usernames and passwords; assign USER on public registration.

### Task 2: Issue and validate JWTs

**Files:** `dto/LoginRequest.java`, `dto/AuthResponse.java`, `service/JwtTokenService.java`, `config/SecurityConfiguration.java`

- [x] Add a one-hour HS256 token with issuer, subject, and role claims.
- [x] Validate signed Bearer tokens with Spring Security Resource Server.
- [x] Use an environment-provided signing secret or a random per-process key.

### Task 3: Secure APIs and provision administrators

**Files:** `controller/AuthController.java`, `component/AdminUserInitializer.java`, `config/SecurityConfiguration.java`

- [x] Expose registration and login; keep invalid-credential responses generic.
- [x] Enable HTTP Basic and Bearer JWT authentication.
- [x] Restrict reads to signed-in USER/ADMIN and writes to ADMIN.
- [x] Bootstrap ADMIN only from paired environment variables.

### Task 4: Document Lab 9

**Files:** `docs/lab9.md`, `src/main/resources/application.properties`

- [x] Document endpoints, roles, security headers, credentials, configuration, and response codes.
- [x] Verification: `mvn -q -DskipTests package`.
