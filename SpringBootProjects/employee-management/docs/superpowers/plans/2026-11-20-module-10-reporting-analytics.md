# Module 10: Reporting and Analytics Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Provide total employee and per-department statistics through REST and a Thymeleaf page.

**Architecture:** Add aggregate JPQL queries to `EmployeeRepository` and return a department-count DTO from `EmployeeReportService`. Reuse the existing one-minute cached total count. Expose the department aggregate from the existing report controller and render both reports in an ADMIN-only page.

**Tech Stack:** Spring Data JPA `@Query`, Spring MVC, Thymeleaf, existing employee-count Caffeine cache.

**Spec:** User request for Module 10; documentation in `docs/lab10.md`.

## Global Constraints

- Preserve the existing Module 9 rule that USER only sees employee lists/search results and ADMIN owns administrative reporting.
- Keep the total employee count cached for one minute and rely on existing employee CRUD eviction.
- Include departments with zero employees in the grouped report.

## Review Focus

- Departments with no employees still appear with count zero.
- Empty system has total zero and no divide-by-zero in the page.
- API and page use the same repository-backed aggregate results.
- USER cannot access the report API or statistics page.

---

### Task 1: Repository aggregate queries

**Files:** `repository/EmployeeRepository.java`, `dto/DepartmentEmployeeCountResponse.java`

- [x] Add a JPQL total count query and department left-join aggregate grouped by department.

### Task 2: Shared reporting service and REST endpoint

**Files:** `service/EmployeeReportService.java`, `controller/EmployeeReportController.java`

- [x] Reuse the cached system total and expose counts by department at `GET /api/employees/report/by-department`.

### Task 3: Statistics page and access rule

**Files:** `controller/EmployeePageController.java`, `config/SecurityConfiguration.java`, `templates/employees/statistics.html`, `static/css/site.css`

- [x] Render system total and per-department counts at `/employees/statistics` for ADMIN.
- [x] Add an ADMIN-only link to the statistics page.

### Task 4: Lab documentation and verification

**Files:** `docs/lab10.md`

- [x] Document the aggregate endpoint, page, query behavior, and role access.
- [x] Verification: `mvn -q -DskipTests package`.
