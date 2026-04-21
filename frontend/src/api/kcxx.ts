import { http } from './http'
import { normalizePageResult, unwrap, type PageResult } from './result'

export type Kcxx = {
  kid?: number
  drugsName: string
  num: number
  rid: number
  marks?: string
}

export async function listKcxx(params?: {
  page?: number
  size?: number
  sortField?: string
  sortOrder?: 'asc' | 'desc'
}) {
  const data = await unwrap<unknown>(http.get('/kcxx/list', { params }))
  return normalizePageResult<Kcxx>(data) as PageResult<Kcxx>
}

export async function listWarning() {
  return unwrap<Kcxx[]>(http.get('/kcxx/getWarningList'))
}

export async function addKcxx(body: Kcxx) {
  return unwrap<string>(http.post('/kcxx/add', body))
}

export async function updateKcxx(body: Kcxx) {
  return unwrap<string>(http.post('/kcxx/update', body))
}

export async function deleteKcxx(kid: number) {
  return unwrap<string>(http.get('/kcxx/delete', { params: { kid } }))
}
