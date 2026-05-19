def assert_success_page(response, min_total: int = 0):
    response.raise_for_status()
    payload = response.json()
    assert payload["code"] == 200, payload
    assert payload["data"]["total"] >= min_total, payload
    assert isinstance(payload["data"]["items"], list), payload


def assert_success_list(response, min_items: int = 0):
    response.raise_for_status()
    payload = response.json()
    assert payload["code"] == 200, payload
    assert isinstance(payload["data"], list), payload
    assert len(payload["data"]) >= min_items, payload


def test_admin_can_read_core_business_pages(api_get, admin_headers: dict[str, str]):
    endpoints = [
        "/api/user/list",
        "/api/drugs/list",
        "/api/category/list",
        "/api/kcxx/list",
        "/api/purchase/list",
        "/api/sale/list",
        "/api/audit/list",
    ]

    for path in endpoints:
        response = api_get(path, headers=admin_headers, page=1, size=5)
        assert_success_page(response, min_total=1)


def test_employee_can_read_allowed_business_pages(api_get, employee_headers: dict[str, str]):
    endpoints = [
        "/api/drugs/list",
        "/api/category/list",
        "/api/kcxx/list",
        "/api/purchase/list",
        "/api/sale/list",
    ]

    for path in endpoints:
        response = api_get(path, headers=employee_headers, page=1, size=5)
        assert_success_page(response, min_total=1)


def test_statistics_and_reports_return_data(api_get, admin_headers: dict[str, str]):
    for path in [
        "/api/statistic/getSalePie",
        "/api/statistic/getPurchaseBar",
        "/api/report/sales/drug",
        "/api/report/sales/day",
        "/api/report/purchase/drug",
        "/api/report/inventory/low",
    ]:
        response = api_get(path, headers=admin_headers)
        assert_success_list(response)
