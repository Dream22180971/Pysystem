import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

/** 路由：除 meta.public 外需已登录（Pinia 中 token） */
export const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('../pages/LoginPage.vue'),
      meta: { public: true },
    },
    {
      path: '/',
      component: () => import('../layouts/AppLayout.vue'),
      children: [
        {
          path: '',
          name: 'dashboard',
          component: () => import('../pages/DashboardPage.vue'),
          meta: { title: '智能看板' },
        },
        {
          path: 'employees',
          name: 'employees',
          component: () => import('../pages/EmployeesPage.vue'),
          meta: { title: '员工管理' },
        },
        {
          path: 'drugs',
          name: 'drugs',
          component: () => import('../pages/DrugsPage.vue'),
          meta: { title: '药品管理' },
        },
        {
          path: 'categories',
          name: 'categories',
          component: () => import('../pages/CategoriesPage.vue'),
          meta: { title: '分类管理' },
        },
        {
          path: 'sales',
          name: 'sales',
          component: () => import('../pages/SalesPage.vue'),
          meta: { title: '销售管理' },
        },
        {
          path: 'inventory',
          name: 'inventory',
          component: () => import('../pages/InventoryPage.vue'),
          meta: { title: '库存管理' },
        },
        {
          path: 'purchase',
          name: 'purchase',
          component: () => import('../pages/PurchasePage.vue'),
          meta: { title: '采购管理' },
        },
        {
          path: 'stats',
          name: 'stats',
          component: () => import('../pages/StatsPage.vue'),
          meta: { title: '智能统计' },
        },
        {
          path: 'audit-logs',
          name: 'audit-logs',
          component: () => import('../pages/AuditLogPage.vue'),
          meta: { title: '日志审计' },
        },
      ],
    },
  ],
})

router.beforeEach((to) => {
  // 未登录访问受保护路由 → 跳转登录并带上 redirect
  const auth = useAuthStore()
  if (to.meta.public) return true
  if (auth.isAuthed) return true
  return { name: 'login', query: { redirect: to.fullPath } }
})
