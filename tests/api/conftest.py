import os
from collections.abc import Generator

import pytest
import requests


DEFAULT_TIMEOUT = float(os.getenv("API_TEST_TIMEOUT", "10"))


def join_url(base_url: str, path: str) -> str:
    return f"{base_url.rstrip('/')}/{path.lstrip('/')}"


@pytest.fixture(scope="session")
def base_url() -> str:
    return os.getenv("API_BASE_URL", "http://localhost:8080")


@pytest.fixture(scope="session")
def http() -> Generator[requests.Session, None, None]:
    session = requests.Session()
    yield session
    session.close()


def login(session: requests.Session, base_url: str, username: str, password: str, expected_pid: int) -> dict:
    response = session.post(
        join_url(base_url, "/api/auth/login"),
        json={"username": username, "password": password, "expectedPId": expected_pid},
        timeout=DEFAULT_TIMEOUT,
    )
    response.raise_for_status()
    payload = response.json()
    assert payload["code"] == 200, payload
    assert payload["data"]["token"], payload
    return payload["data"]


@pytest.fixture(scope="session")
def admin_auth(http: requests.Session, base_url: str) -> dict:
    return login(http, base_url, "admin", "admin123", 1)


@pytest.fixture(scope="session")
def employee_auth(http: requests.Session, base_url: str) -> dict:
    # emp01 is intentionally not used: this local DB has a changed password for that seed row.
    return login(http, base_url, "emp02", "employee123", 2)


@pytest.fixture(scope="session")
def admin_headers(admin_auth: dict) -> dict[str, str]:
    return {"Authorization": f"Bearer {admin_auth['token']}"}


@pytest.fixture(scope="session")
def employee_headers(employee_auth: dict) -> dict[str, str]:
    return {"Authorization": f"Bearer {employee_auth['token']}"}


@pytest.fixture(scope="session")
def api_get(base_url: str, http: requests.Session):
    def get(path: str, headers: dict[str, str] | None = None, **params):
        return http.get(join_url(base_url, path), headers=headers, params=params, timeout=DEFAULT_TIMEOUT)

    return get
