# Lab 6: Spring MVC and Thymeleaf

The HTML pages use Thymeleaf templates under `src/main/resources/templates` and share the existing `EmployeeService` and `DepartmentService`. The REST endpoints under `/api` remain available.

- `GET /employees/list` displays employees from MySQL.
- `GET /employees/add` displays the employee form; `POST /employees/add` binds and validates it, saves through `EmployeeService`, then redirects to the list.
- `GET /employees/search` searches by optional `name` and `departmentId` query parameters and displays matching employees.

Create at least one department through `POST /api/departments` before adding an employee. The add form lists available departments and shows field validation messages when input is invalid.
