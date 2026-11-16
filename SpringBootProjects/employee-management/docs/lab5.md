# Lab 5: Validation and Exception Handling

Employee requests use Bean Validation: `name` must not be blank and is limited to 120 characters; `email` must be a valid email and is limited to 254 characters; `departmentId` must be positive. `@Valid` runs validation before the controller proceeds. Employee create/update endpoints inspect `BindingResult` and send field errors to the global exception handler.

## Validation error example

Sending an empty name or invalid email returns HTTP 400:

```json
{
  "code": "VALIDATION_ERROR",
  "message": "Request validation failed",
  "details": {
    "name": "must not be blank",
    "email": "must be a well-formed email address"
  }
}
```

Malformed JSON or a value that cannot be converted to the declared type returns HTTP 400 with `code: INVALID_REQUEST`.

## Not found example

Requesting a missing employee, for example `GET /api/employees/999999`, returns HTTP 404:

```json
{
  "code": "NOT_FOUND",
  "message": "Employee not found",
  "details": {}
}
```

`GlobalExceptionHandler` applies the same error shape across controllers for validation, malformed input, parameter conversion, and `ResponseStatusException` errors such as a missing employee. Other framework errors keep their standard HTTP status handling.
