import { http } from './http'
import { unwrap } from './result'

export type AuditLog = {
  id: number
  operator: string
  module: string
  action: string
  detail: string | null
  ip: string | null
  createdAt: string
}

export async function listAuditLogs() {
  return unwrap<AuditLog[]>(http.get('/audit/list'))
}
