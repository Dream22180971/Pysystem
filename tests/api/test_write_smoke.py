import time


def test_admin_can_create_and_delete_auto_test_category(base_url: str, http, admin_headers: dict[str, str]):
    category_name = f"AUTO_TEST_CATEGORY_{int(time.time())}"

    add_response = http.post(
        f"{base_url}/api/category/add",
        headers=admin_headers,
        json={"categoryName": category_name, "status": 1},
        timeout=10,
    )
    add_response.raise_for_status()
    assert add_response.json()["code"] == 200

    category_id = None
    try:
        list_response = http.get(
            f"{base_url}/api/category/list",
            headers=admin_headers,
            params={"page": 1, "size": 200, "sortField": "categoryId", "sortOrder": "desc"},
            timeout=10,
        )
        list_response.raise_for_status()
        payload = list_response.json()
        matches = [item for item in payload["data"]["items"] if item["categoryName"] == category_name]
        assert matches, payload
        category_id = matches[0]["categoryId"]
    finally:
        if category_id is not None:
            delete_response = http.get(
                f"{base_url}/api/category/delete",
                headers=admin_headers,
                params={"categoryId": category_id},
                timeout=10,
            )
            delete_response.raise_for_status()
            assert delete_response.json()["code"] == 200
