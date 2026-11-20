# Lab 10: Reporting and Analytics

## Reports

- `GET /api/employees/report/count` returns `{"totalEmployees":N}`. It uses a JPQL `@Query` and the existing 60-second cache, invalidated by employee create, update, and delete operations.
- `GET /api/employees/report/by-department` returns an array with `departmentId`, `departmentName`, and `employeeCount`. The left join includes departments that currently have no employees.
- `/employees/statistics` renders the system total, department count, per-department employee counts, and each department's share of the total. The page derives its total from the grouped query so the displayed total and department rows share one database read.

The statistics API and page are restricted to `ADMIN` under the Module 9 authorization policy. A `USER` continues to have access to the employee list and search pages.

## Example response

```json
[
  {"departmentId":1,"departmentName":"Engineering","employeeCount":8},
  {"departmentId":2,"departmentName":"Finance","employeeCount":0}
]
```

Department counts are calculated from employee and department rows using Spring Data JPA `@Query`; the page and REST API read from the same reporting service.
