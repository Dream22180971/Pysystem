import { http } from './http'
import { normalizePageResult, unwrap, type PageResult } from './result'

export type Drugs = {
  id?: number
  drugsName: string
  nums: number
  drugsImage: string
  categoryId: number
  price: number
  people: string
  useMethod: string
  cid: number
  rid: number
  productTime: string
  saveTime: number
  status: number
}

export async function listDrugs(params?: { page?: number; size?: number; sortField?: string; sortOrder?: 'asc' | 'desc' }) {
  const data = await unwrap<unknown>(http.get('/drugs/list', { params }))
  return normalizePageResult<Drugs>(data) as PageResult<Drugs>
}

export async function addDrug(body: Drugs) {
  return unwrap<string>(http.post('/drugs/add', body))
}

export async function updateDrug(body: Drugs) {
  return unwrap<string>(http.post('/drugs/update', body))
}

export async function deleteDrug(id: number) {
  return unwrap<string>(http.get('/drugs/delete', { params: { id } }))
}
