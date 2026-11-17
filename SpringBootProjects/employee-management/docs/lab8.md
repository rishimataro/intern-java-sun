# Lab 8: Actuator, Scheduling, and Caching

- `GET /actuator/health` returns application health.
- `GET /actuator/metrics` lists available metrics; append a metric name to inspect its measurements.
- `GET /api/employees/report/count` returns `{"totalEmployees":N}`. The count is cached for 60 seconds and the cache is evicted after successful employee create, update, or delete operations.
- `SystemRunningScheduler` logs `System running` every 30 seconds through SLF4J/Logback.

Only the health and metrics Actuator endpoints are exposed. Keep these monitoring endpoints behind appropriate network or access controls when deploying beyond a trusted development environment.
