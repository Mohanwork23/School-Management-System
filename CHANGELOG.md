# Changelog (recent daily updates)

## 2026-08-07 — Day 8
- Added JaCoCo code coverage reporting to Maven build.
- Configured CI to upload coverage reports to Codecov for visibility.
- Set up coverage analysis during test phase.

## 2026-08-08 — Day 9
- Added a lightweight `GET /api/status` endpoint returning app name, status, and timestamp for simple demos and readiness checks.
- Added integration test to verify `/api/status` is accessible and returns `status: OK`.

## 2026-08-06 — Day 7
- Added Maven Checkstyle support with the Google style guide.
- Updated CI to run `mvn verify` so style violations are caught automatically.

## 2026-08-05 — Day 6
- Enabled unauthenticated access to `/actuator/health` and `/actuator/info` for readiness monitoring.
- Added an integration test for the Actuator health endpoint.
- Documented actuator health availability in `README.md`.

## 2026-08-04 — Day 5
- Added a security scan workflow (`scan-image.yml`) using Trivy to validate published container images.
- Added a GitHub Actions Docker publish workflow (`docker-publish.yml`) and README badge.
- Improved deployment documentation with GHCR and Docker Compose usage.

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
