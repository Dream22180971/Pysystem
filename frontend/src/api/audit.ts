import { http } from './http'
import { normalizePageResult, unwrap, type PageResult } from './result'

export type AuditLog = {
  id: number
  operator: string
  module: string
  action: string
  detail: string | null
  ip: string | null
  createdAt: string
}

export async function listAuditLogs(params?: { page?: number; size?: number; sortField?: string; sortOrder?: 'asc' | 'desc' }) {
  const data = await unwrap<unknown>(http.get('/audit/list', { params }))
  return normalizePageResult<AuditLog>(data) as PageResult<AuditLog>
}
