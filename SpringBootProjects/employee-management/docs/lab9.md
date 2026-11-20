# Lab 9: Spring Security Basics

## Concepts

- **Authentication** checks who is making a request; registration and login verify credentials.
- **Authorization** checks what an authenticated user is allowed to do; this lab uses `USER` and `ADMIN` roles.
- **HTTP Basic** sends a username/password credential with each request. It is useful for simple learning and should only be used over HTTPS outside local development.
- **JWT** is a signed bearer token returned after login. This lab puts the username and role in the token and expires it after one hour; the API verifies the signature and claims on each request.
- The database stores BCrypt password hashes. A password encoder verifies a submitted password without storing the original text.

## Authentication and roles

- `POST /api/auth/register` creates an account with the `USER` role and returns a one-hour JWT.
- `POST /api/auth/login` checks the username and password and returns a JWT.
- Passwords are stored as BCrypt hashes. Public registration cannot choose a role.
- HTTP Basic is enabled for learning and quick read-only API calls. Use HTTPS outside local development.
- Protected API requests accept `Authorization: Bearer <accessToken>`.

## Authorization

- `USER` and `ADMIN` can view the employee list at `GET /api/employees` and the Thymeleaf list/search pages.
- Only `ADMIN` can read other `/api/**` routes and write to `/api/**`, including employee create, update, and delete.
- `/employees/list` and `/employees/search` require a signed-in user. `/employees/add` requires `ADMIN`.
- Thymeleaf forms keep CSRF protection enabled. Bearer-token API calls do not use cookies and bypass CSRF; Basic-authenticated write calls still require a CSRF token.
- `/actuator/health` is public; other application routes, including metrics, require authentication.
- A per-process in-memory limiter allows up to 120 requests per minute per remote address, with a stricter 10 requests per minute for login/registration and HTTP Basic attempts. Use a shared gateway or distributed limiter when running multiple instances. The address is the servlet connection peer; configure a trusted proxy or gateway for deployments behind a load balancer.

The prod profile keeps Hibernate schema validation enabled and does not run DDL by default. Apply `src/main/resources/db/schema-user.sql` once with a database migration account before deploying Module 9. For a disposable lab environment, set `DB_SCHEMA_INIT_MODE=always`; for a production runtime account without DDL privileges, leave the default `never`.

## Create an administrator

Set both environment variables before starting the app. The initializer creates the account only when it does not already exist, and never changes an existing password or role.

```powershell
$env:ADMIN_USERNAME = "admin"
$env:ADMIN_PASSWORD = "Use-a-local-password-with-at-least-8-characters"
```

The password is BCrypt hashed before it is stored. Do not use these example values as real credentials. If the username already belongs to a non-admin user, startup stops rather than promoting that account.

## JWT signing key

For a stable signing key across restarts, set `JWT_SECRET` to a random value of at least 32 UTF-8 bytes. A value prefixed with `base64:` is decoded before the size check. If unset, a cryptographically random key is generated for this process and a warning is logged; all previously issued tokens stop working after restart. Provide a managed secret in deployed environments.

```powershell
$env:JWT_SECRET = "replace-this-with-a-random-secret-of-at-least-32-bytes"
```

## Example requests

Register a regular user:

```http
POST /api/auth/register
Content-Type: application/json

{"username":"learner1","password":"S0mething-long-enough"}
```

Login:

```http
POST /api/auth/login
Content-Type: application/json

{"username":"learner1","password":"S0mething-long-enough"}
```

Read employee data with the returned JWT:

```http
GET /api/employees
Authorization: Bearer <accessToken>
```

Use an admin JWT to create, update, or delete employees. HTTP Basic is available for checking read access, for example `curl.exe -u learner1:S0mething-long-enough http://localhost:8080/api/employees`. Basic-authenticated write calls also need a CSRF token; API clients should use Bearer JWTs.

## Status codes

- `201 Created` when registration succeeds.
- `200 OK` when login succeeds.
- `400 Bad Request` for invalid registration or login fields.
- `401 Unauthorized` for a missing or invalid credential on a protected route.
- `403 Forbidden` when a valid user lacks the `ADMIN` role, or when an unsafe request is rejected by CSRF before authentication or authorization completes.
- `409 Conflict` when the username is already registered.
- `429 Too Many Requests` when the per-address rate limit is exceeded.
