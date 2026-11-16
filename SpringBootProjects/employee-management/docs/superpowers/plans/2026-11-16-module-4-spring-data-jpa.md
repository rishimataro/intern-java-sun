# Module 4: Spring Data JPA Implementation Plan

**Goal:** Replace in-memory employee storage with MySQL-backed Spring Data JPA and add employee/department CRUD and search.

**Architecture:** Use Spring MVC for the existing REST controllers because JPA access is blocking. Model `Employee` as a many-to-one relation to `Department`; keep persistence in `JpaRepository` interfaces, business operations in services, and expose DTOs rather than serializing entities directly.

**Tech Stack:** Spring Boot 4.1.2, Spring MVC, Spring Data JPA, Hibernate, MySQL Connector/J, Java 25.

**Scope decisions:**
- Employee primary key is a database-generated `Long`; columns are `id`, `name`, `email`, and `department_id`.
- Department has generated `Long id` and required `name`.
- Add minimal department list/create endpoints so clients can create a department before assigning employees.
- Employee search accepts optional `name` and `departmentId` query parameters; either or both can be used.
- MySQL URL, username, and password come from environment-backed Spring properties; Hibernate uses `ddl-auto=update` for this lab.
- Preserve `/hello` and the existing employee-code demo endpoint.

**Review focus:**
- Missing or blank required fields return HTTP 400.
- Missing employee or department IDs return HTTP 404.
- Creating/updating an employee with a nonexistent department returns HTTP 404.
- Search works with no filters, either filter, and both filters.
- API responses do not expose lazy JPA proxies or recurse across the employee/department relation.

## Implementation steps

1. Replace the WebFlux starter with Spring MVC and add Spring Data JPA and MySQL Connector/J.
2. Convert Employee to a JPA entity and add Department entity, relationship mapping, request/response DTOs, and JpaRepository interfaces.
3. Implement transactional employee CRUD and optional name/department search; add minimal department create/list support.
4. Update REST controllers for CRUD response statuses and configure MySQL connection settings through environment variables; document local startup settings.
5. Run `mvn -q -DskipTests package`, inspect the final diff, then commit and push a feature branch and open a PR.
