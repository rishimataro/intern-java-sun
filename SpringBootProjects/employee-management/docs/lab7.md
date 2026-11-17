# Lab 7: Logging and Profiles

Spring Boot starters include SLF4J and Logback. `EmployeeService` logs employee creation, update, and deletion at `INFO` level, using employee and department IDs without logging names or email addresses. Spring Boot writes these logs to the console by default.

The application uses the `dev` profile by default. `application-dev.yml` points at a local MySQL instance and enables Hibernate schema updates and SQL output for development. Activate `prod` with `SPRING_PROFILES_ACTIVE=prod`; set `DB_URL`, `DB_USERNAME`, and `SPRING_DATASOURCE_PASSWORD` in the production environment. Spring Boot reads the password directly from that environment variable without storing it in the profile file. The production profile validates the schema and disables SQL output. The database schema must be created before starting with `prod`.
