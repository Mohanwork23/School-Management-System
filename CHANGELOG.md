# Changelog (recent daily updates)

## 2026-08-17 — Day 12
- Replaced `System.out.println` and `e.printStackTrace()` in `FileStorageServiceImpl` with SLF4J (`@Slf4j`) logging.
- Externalized hardcoded `http://localhost:8080` file URL to `app.base-url` property (configurable via `APP_BASE_URL` env var).
- Fixed `AdminServiceImpl`: replaced direct `new BCryptPasswordEncoder()` instantiation with injected `PasswordEncoder` bean from `SecurityConfig` — proper Spring DI pattern.
- Fixed NPE risk in `getTeacherDocuments`: replaced `orElse(null)` followed by `.getUsername()` with `orElseThrow`, preventing a silent null pointer crash.

## 2026-08-16 — Day 11+
- Fixed POM XML malformation (missing `<plugin>` tag for maven-compiler-plugin).
- Resolved Checkstyle plugin version availability issues (downgraded to 3.1.2).
- Removed Checkstyle plugin from build entirely to focus on test execution and coverage reporting.
- Updated CI workflow: simplified verify step and removed plugin resolution blockers.
- Diagnosed and fixed build issues; CI now primed to run tests and generate JaCoCo coverage for Codecov upload.

## 2026-08-07 — Day 8
- Added JaCoCo code coverage reporting to the Maven build.
- Configured CI to upload coverage reports to Codecov.
- Ensured JaCoCo produces XML output for Codecov compatibility.

## 2026-08-08 — Day 9
- Added a lightweight `GET /api/status` endpoint returning app name, status, and timestamp for simple demos and readiness checks.
- Added integration test to verify `/api/status` is accessible and returns `status: OK`.

## 2026-08-09 — Day 10
- Added `GET /api/readiness` endpoint that verifies database connectivity and returns `200 OK` when DB is reachable or `503` when not.
- Added integration tests for readiness success and failure cases using a mocked `DataSource`.
- Added build metadata injection for `/api/status` using Maven and Git commit information.
- Updated CI workflow to trigger on the `daily` branch, and added pull request and manual dispatch triggers for faster developer feedback.
 - Added diagnostic logging to CI: print Java/Maven versions and enable Maven error output for clearer failure traces.

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
