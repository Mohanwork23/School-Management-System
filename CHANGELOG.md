# Changelog (recent daily updates)

## 2026-08-03 — Day 4
- Added Flyway DB migration support and created initial baseline migration `V1__create_initial_schema.sql`.
- Added `docker-compose.yml` to run MySQL + the app locally for demos.
- Enabled Actuator endpoints and documented health/metrics in `application-dev.properties`.

## 2026-08-02 — Day 3
- Added a Spring context smoke test `SmsApplicationTest` and wired CI to run tests.
- Updated CI workflow to run `mvn test` before packaging.

## 2026-08-01 — Day 2
- Added Dockerfile and `.dockerignore`.
- Added Actuator dependency and exposed endpoints in dev profile.
- Updated `README.md` with Docker run instructions and a resume-ready summary.


# Notes
- These daily changes are intended to make the project easier to demo and production-ready for resume demonstrations.
