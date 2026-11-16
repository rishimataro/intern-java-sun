# Lab 4: MySQL and Spring Data JPA

## Run with MySQL

Start a local MySQL server. Set `DB_USERNAME` and `DB_PASSWORD` in the environment where the app runs. The default JDBC URL is `jdbc:mysql://localhost:3306/employee_management?createDatabaseIfNotExist=true&serverTimezone=UTC`; override it with `DB_URL` when the database is elsewhere. No database password is stored in the repository.

Hibernate uses `ddl-auto=update` for this lab to create or update the `employee` and `department` tables. Use a schema migration tool for production systems.

## Employee API

- `GET /api/employees` — list all employees.
- `GET /api/employees?name=an` — search by a partial, case-insensitive name.
- `GET /api/employees?departmentId=1` — search by department ID. Both filters can be combined.
- `GET /api/employees/{id}` — get one employee.
- `POST /api/employees` — create an employee.
- `PUT /api/employees/{id}` — replace an employee's name, email, and department.
- `DELETE /api/employees/{id}` — delete an employee.

Create a department first with `POST /api/departments` and JSON such as `{"name":"Engineering"}`. Use the returned ID in an employee request, for example `{"name":"Ada Lovelace","email":"ada@example.com","departmentId":1}`. `GET /api/departments` lists departments.

Employee responses have the shape `{"id":1,"name":"Ada Lovelace","email":"ada@example.com","departmentId":1,"departmentName":"Engineering"}`.
