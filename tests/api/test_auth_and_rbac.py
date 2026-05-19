import requests


def test_health_check_is_up(base_url: str):
    response = requests.get(f"{base_url}/actuator/health", timeout=10)
    response.raise_for_status()
    assert response.json()["status"] == "UP"


def test_admin_login_returns_admin_role(admin_auth: dict):
    assert admin_auth["username"] == "admin"
    assert admin_auth["role"] == "ROLE_ADMIN"
    assert admin_auth["pId"] == 1


def test_employee_login_returns_employee_role(employee_auth: dict):
    assert employee_auth["username"] == "emp02"
    assert employee_auth["role"] == "ROLE_EMP"
    assert employee_auth["pId"] == 2


def test_login_rejects_wrong_password(base_url: str, http: requests.Session):
    response = http.post(
        f"{base_url}/api/auth/login",
        json={"username": "admin", "password": "wrong-password", "expectedPId": 1},
        timeout=10,
    )
    assert response.status_code == 200
    payload = response.json()
    assert payload["code"] == 401
    assert payload["data"] is None


def test_anonymous_business_api_is_blocked(api_get):
    response = api_get("/api/drugs/list", page=1, size=5)
    assert response.status_code == 401
    assert response.json()["code"] == 401


def test_employee_cannot_access_admin_only_apis(api_get, employee_headers: dict[str, str]):
    for path in ["/api/user/list", "/api/audit/list"]:
        response = api_get(path, headers=employee_headers, page=1, size=5)
        assert response.status_code == 403
        assert response.json()["code"] == 403
