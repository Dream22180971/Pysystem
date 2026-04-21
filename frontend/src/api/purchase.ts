import { http } from './http'
import { normalizePageResult, unwrap, type PageResult } from './result'

export type Purchase = {
  pid?: number
  drugsName: string
  num: number
  indate: string
  rid: number
  marks?: string
}

export async function listPurchases(params?: { page?: number; size?: number; sortField?: string; sortOrder?: 'asc' | 'desc' }) {
  const data = await unwrap<unknown>(http.get('/purchase/list', { params }))
  return normalizePageResult<Purchase>(data) as PageResult<Purchase>
}

export async function addPurchase(body: Purchase) {
  return unwrap<string>(http.post('/purchase/add', body))
}

export async function updatePurchase(body: Purchase) {
  return unwrap<string>(http.post('/purchase/update', body))
}

export async function deletePurchase(pid: number) {
  return unwrap<string>(http.get('/purchase/delete', { params: { pid } }))
}
