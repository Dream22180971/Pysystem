import { expect, type Page, test } from '@playwright/test'

const apiBaseURL = process.env.E2E_API_BASE_URL ?? 'http://127.0.0.1:8080'

async function login(page: Page, username: string, password: string, expectedPId: 1 | 2) {
  await page.goto('/login')
  await page.getByTestId(`role-${expectedPId}`).click()
  await page.getByPlaceholder('请输入用户名').fill(username)
  await page.getByPlaceholder('请输入密码').fill(password)
  await page.getByRole('button', { name: '登录' }).click()
}

async function loginByApi(page: Page, username: string, password: string, expectedPId: 1 | 2) {
  const response = await page.request.post(`${apiBaseURL}/api/auth/login`, {
    data: { username, password, expectedPId },
  })
  expect(response.ok()).toBeTruthy()
  const payload = await response.json()
  expect(payload.code).toBe(200)

  await page.goto('/login')
  await page.evaluate((auth) => {
    localStorage.setItem('pysystem.auth', JSON.stringify(auth))
  }, {
    token: payload.data.token,
    username: payload.data.username,
    role: payload.data.role,
    pId: payload.data.pId,
  })
}

test('login page renders the role-aware sign-in form', async ({ page }) => {
  await page.goto('/login')

  await expect(page.getByTestId('role-1')).toBeVisible()
  await expect(page.getByTestId('role-2')).toBeVisible()
  await expect(page.getByPlaceholder('请输入用户名')).toBeVisible()
  await expect(page.getByPlaceholder('请输入密码')).toBeVisible()
  await expect(page.getByRole('button', { name: '登录' })).toBeVisible()
})

test('admin can sign in and see admin navigation', async ({ page }) => {
  await loginByApi(page, 'admin', 'admin123', 1)
  await page.goto('/')

  await expect(page).toHaveURL('/')
  await expect(page.getByTestId('app-shell')).toBeVisible()
  await expect(page.getByTestId('menu-employees')).toBeVisible()
  await expect(page.getByTestId('menu-audit-logs')).toBeVisible()
})

test('wrong password stays on login page and shows an error', async ({ page }) => {
  await login(page, 'admin', 'wrong-password', 1)

  await expect(page).toHaveURL('/login')
  await expect(page.locator('.error')).toBeVisible()
})

test('employee can access business pages but cannot access admin-only pages', async ({ page }) => {
  await loginByApi(page, 'emp02', 'employee123', 2)

  await page.goto('/drugs')
  await expect(page.getByTestId('app-shell')).toBeVisible()
  await expect(page.getByTestId('menu-drugs')).toBeVisible()
  await expect(page.getByTestId('menu-purchase')).toBeVisible()
  await expect(page.getByTestId('menu-employees')).toHaveCount(0)
  await expect(page.getByTestId('menu-audit-logs')).toHaveCount(0)

  await page.goto('/employees')
  await expect(page).toHaveURL('/')
})

test('admin protected pages render from the router', async ({ page }) => {
  await loginByApi(page, 'admin', 'admin123', 1)

  for (const route of ['/', '/drugs', '/categories', '/inventory', '/purchase', '/sales', '/stats', '/audit-logs', '/employees']) {
    await page.goto(route)
    await expect(page.getByTestId('app-shell')).toBeVisible()
    await expect(page).not.toHaveURL('/login')
  }
})
