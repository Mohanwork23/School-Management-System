# School Management System (SMS)

This is a Spring Boot monolithic application for managing school data (students, teachers, classes, attendance, fees, timetables, etc.).

Getting started

- Set environment variables for sensitive values (recommended):

  - `DB_USERNAME`, `DB_PASSWORD` — database credentials
  - `JWT_SECRET` — JWT signing secret (use a strong random value, minimum 32 chars)
  - `MAIL_USERNAME`, `MAIL_PASSWORD` — SMTP credentials

- Build and run locally:

```bash
mvn clean package
java -jar target/SMS-0.0.1-SNAPSHOT.jar
```

CI

- A GitHub Actions workflow runs on push and nightly to run tests and package the app: see `.github/workflows/maven-ci.yml`.

API Docs

- Swagger UI is available when running the app at `/swagger-ui.html` (provided by springdoc-openapi).
