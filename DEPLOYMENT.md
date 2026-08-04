# Deployment Guide

## Overview
This project can be deployed locally, in containerized environments, or to GitHub Container Registry.

## Local development with Docker Compose

1. Build and start the stack:

```bash
docker compose up --build
```

2. Access the application at:

```text
http://localhost:8080
```

3. Use the database container on port `3306` with credentials:

- `MYSQL_ROOT_PASSWORD=root`
- `MYSQL_DATABASE=sms`
- `MYSQL_USER=smsuser`
- `MYSQL_PASSWORD=smspass`

## Build and run locally without Docker

```bash
mvn clean package -DskipTests
java -jar target/SMS-0.0.1-SNAPSHOT.jar
```

## Publish to GitHub Container Registry

When code is pushed to `main` or `daily`, GitHub Actions publishes container images to GHCR:

- `ghcr.io/<owner>/school-management-system:latest`
- `ghcr.io/<owner>/school-management-system:<commit-sha>`

## Run the published image locally

```bash
docker run -e DB_URL=jdbc:mysql://host.docker.internal:3306/sms \
  -e DB_USERNAME=smsuser \
  -e DB_PASSWORD=smspass \
  -e JWT_SECRET=replace-with-a-long-secret \
  -p 8080:8080 \
  ghcr.io/<owner>/school-management-system:latest
```

## Notes
- In production, use a secret manager for `JWT_SECRET` and email credentials.
- Replace `host.docker.internal` with the database host used by your deployment environment.
