# Automated Testing

This project now has a lightweight automation stack for API tests, browser E2E tests, Docker-based runtime checks, and GitHub Actions CI.

## Prerequisites

- JDK 17
- Maven 3.6+
- Node.js 24+
- Python 3.12+
- MySQL 8 with the `pharmacy_system` schema loaded

Default local credentials used by the tests:

- MySQL: `root / admin123`
- Admin user: `admin / admin123`
- Employee user: `emp02 / employee123`

`emp01` is intentionally not used by tests because the current local database has a changed password for that account.

## Local Verification

Build backend:

```bash
mvn -DskipTests clean package
```

Build frontend:

```bash
cd frontend
npm install
npm run build
```

Run API tests against a running backend:

```bash
pip install -r requirements.txt
pytest tests/api
```

Run Playwright E2E tests against a running backend:

```bash
cd frontend
npm install
npx playwright install chromium
npm run test:e2e
```

Optional environment variables:

```bash
API_BASE_URL=http://localhost:8080
E2E_BASE_URL=http://127.0.0.1:5173
E2E_API_BASE_URL=http://127.0.0.1:8080
```

## Docker

Docker Compose starts MySQL, the Spring Boot backend, and the Nginx-hosted frontend:

```bash
docker compose up -d --build
```

The compose MySQL service publishes host port `3307` to avoid colliding with a local MySQL on `3306`.

Run tests against Docker services:

```bash
pytest tests/api
cd frontend
PLAYWRIGHT_SKIP_WEB_SERVER=1 npm run test:e2e
```

Stop and remove containers and data:

```bash
docker compose down -v
```
