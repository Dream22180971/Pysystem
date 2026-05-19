import type { Role } from '../stores/auth'

/** 路由 name → 允许访问的角色（未列出的路由仅校验已登录） */
export const routeRoles: Partial<Record<string, Role[]>> = {
  dashboard: ['ROLE_ADMIN', 'ROLE_EMP', 'ROLE_USER'],
  employees: ['ROLE_ADMIN'],
  drugs: ['ROLE_ADMIN', 'ROLE_EMP', 'ROLE_USER'],
  categories: ['ROLE_ADMIN', 'ROLE_EMP', 'ROLE_USER'],
  sales: ['ROLE_ADMIN', 'ROLE_EMP'],
  inventory: ['ROLE_ADMIN', 'ROLE_EMP', 'ROLE_USER'],
  purchase: ['ROLE_ADMIN', 'ROLE_EMP'],
  stats: ['ROLE_ADMIN', 'ROLE_EMP', 'ROLE_USER'],
  'audit-logs': ['ROLE_ADMIN'],
}

export type MenuItem = {
  path: string
  title: string
  roles: Role[]
}

export const menuItems: MenuItem[] = [
  { path: '/', title: '智能看板', roles: routeRoles.dashboard! },
  { path: '/employees', title: '员工管理', roles: routeRoles.employees! },
  { path: '/drugs', title: '药品管理', roles: routeRoles.drugs! },
  { path: '/categories', title: '分类管理', roles: routeRoles.categories! },
  { path: '/sales', title: '销售管理', roles: routeRoles.sales! },
  { path: '/inventory', title: '库存管理', roles: routeRoles.inventory! },
  { path: '/purchase', title: '采购管理', roles: routeRoles.purchase! },
  { path: '/stats', title: '数据报表', roles: routeRoles.stats! },
  { path: '/audit-logs', title: '日志审计', roles: routeRoles['audit-logs']! },
]

export function canAccessRoute(role: Role | null, routeName: string | symbol | undefined): boolean {
  if (!routeName || typeof routeName !== 'string') return true
  const allowed = routeRoles[routeName]
  if (!allowed || allowed.length === 0) return true
  if (!role) return false
  return allowed.includes(role)
}
